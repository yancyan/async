package com.example.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.*;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

    @Scheduled(initialDelay = 1000 * 5, fixedDelay = 1000 * 60 * 5)
    public void schedule() throws ExecutionException, InterruptedException {

        log.info("schedule process...");

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
        //     System.out.println("执行step 1");
        //     awaitSeconds(5L);
        //     return "step1 result";
        // }, executor);
        // CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
        //     System.out.println("执行step 2");
        //     awaitSeconds(3L);
        //
        //     return "step2 result";
        // });
        // cf1.thenCombine(cf2, (result1, result2) -> {
        //     System.out.println(result1 + " , " + result2);
        //     System.out.println("执行step 3");
        //     return "step3 result";
        // }).thenAccept(System.out::println);

        // CompletableFuture<String> cf4 = CompletableFuture.completedFuture("step 4");
        // CompletableFuture<String> cf5 = CompletableFuture.supplyAsync(() -> {
        //     awaitSeconds(3L);
        //     return "step 5";
        // });
        // cf4.thenCombine(cf5, (r4, r5) -> {
        //     log.info(r4, r5);
        //     return "";
        // }).thenAccept(System.out::println);

        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("abc");
        log.info("reuslt is " + cf.get());

    }


    public void awaitSeconds(Long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
