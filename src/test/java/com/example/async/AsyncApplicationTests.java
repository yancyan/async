package com.example.async;

import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// @SpringBootTest
class AsyncApplicationTests {

    @Test
    void contextLoads() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("abc");

        System.out.println(cf.get());

        String re = cf.join();
        System.out.println(re);
    }

    /**
     * 首先执行任务 A，然后将任务 A 的结果传递给任务 B
     */
    @Test
    void task_a_then_b() {
        CompletableFuture.runAsync(() -> {}).thenRun(() -> {});
        CompletableFuture.runAsync(() -> {}).thenAccept(resultA -> {});
        CompletableFuture.runAsync(() -> {}).thenApply(resultA -> "resultB");

        /*
        第 4 行用的是 thenRun(Runnable runnable)，任务 A 执行完执行 B，并且 B 不需要 A 的结果。
        第 5 行用的是 thenAccept(Consumer action)，任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 不返回值。
        第 6 行用的是 thenApply(Function fn)，任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值。
         */
        CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {});
        CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {});
        CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB");


        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "resultA")
                .thenApply(resultA -> resultA + " resultB")
                .thenApply(resultB -> resultB + " resultC")
                .thenApply(resultC -> resultC + " resultD");
        System.out.println(cf.join());
        //    上面的代码中，任务 A、B、C、D 依次执行，如果任务 A 抛出异常（当然上面的代码不会抛出异常），
        //    那么后面的任务都得不到执行。如果任务 C 抛出异常，那么任务 D 得不到执行。

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException();
                })
                .exceptionally(ex -> "errorResultA")
                .thenApply(resultA -> resultA + " resultB")
                .thenApply(resultB -> resultB + " resultC")
                .handle((x, thr) -> {
                    if(thr != null){
                        return "error";
                    }
                    return x;
                })
                .thenApply(resultC -> resultC + " resultD");

        System.out.println(future.join());

    }

    @Test
    void task_combine_a_b() {
        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "resultB");

        // thenAcceptBoth 表示后续的处理不需要返回值，而 thenCombine 表示需要返回值。
        cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {});
        cfA.thenCombine(cfB, (resultA, resultB) -> "result A + B");
        //如果你不需要 resultA 和 resultB，那么还可以使用 runAfterBoth 方法
        cfA.runAfterBoth(cfB, () -> {});

    }

    @Test
    void task_tools() {
        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<Integer> cfB = CompletableFuture.supplyAsync(() -> 123);
        CompletableFuture<String> cfC = CompletableFuture.supplyAsync(() -> "resultC");

        // CompletableFuture<Object> future = CompletableFuture.anyOf(cfA, cfB, cfC);
        CompletableFuture<Void> future = CompletableFuture.allOf(cfA, cfB, cfC);
        Object result = future.join();
        System.out.println(result);

    }

}
