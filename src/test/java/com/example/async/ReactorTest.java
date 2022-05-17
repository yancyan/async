package com.example.async;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ReactorTest {

    @Test
    void test() {
        Mono<String> mon = Mono.fromFuture(CompletableFuture.supplyAsync(() -> "zhuyx"));
        // mon.handle()
        mon.subscribe(System.out::println);
        Flux.just("zhu", "yangl").map(x -> x.concat("@qq.com"))
                .filter(x -> true)
                .subscribe(System.out::println);

    }

    @Test
    void test_thread(){
        Flux.just("tom")
                .map(s -> {
                    System.out.println("(concat @qq.com) at [" + Thread.currentThread() + "]");
                    return s.concat("@qq.com");
                })
                .publishOn(Schedulers.newSingle("thread-a"))
                .map(s -> {
                    System.out.println("(concat foo) at [" + Thread.currentThread() + "]");
                    return s.concat("foo");
                })
                .filter(s -> {
                    System.out.println("(startsWith f) at [" + Thread.currentThread() + "]");
                    return s.startsWith("t");
                })
                .publishOn(Schedulers.newSingle("thread-b"))
                .map(s -> {
                    System.out.println("(to length) at [" + Thread.currentThread() + "]");
                    return s.length();
                })
                .subscribeOn(Schedulers.newSingle("source"))
                .subscribe(System.out::println);

    }

    @Test
    void test_create() {
        Flux.range(100, 10).subscribe(System.out::println);
        Flux.interval(Duration.ofSeconds(5)).subscribe(System.out::println);

        Flux.create(sink -> {
            sink.next("ab");
            sink.complete();
        }).subscribe(System.out::println);
    }

    @Test
    void test_operator_1(){
        // Flux.range(100, 50).buffer(6).subscribe(x -> {
        //     System.out.println(x.size());
        // });

        Flux.range(1, 100).reduce((x, y) -> {
            System.out.println("x = " + x + ", y = " + y);
            return 10;
        }).subscribe(System.out::println);
    }
    @Test
    void test_operator_2(){
        Flux.just("a", "b", "c").zipWith(Flux.just("e", "f"), (s1, s2) -> String.format("%s - %s", s1, s2)).subscribe(System.out::println);
    }
    @Test
    void test_operator_3(){

        Flux.just("a", "b", "c")
                .zipWith(Flux.just("e", "f"), (s1, s2) -> String.format("%s - %s", s1, s2))
                .log("zhuyx -> ")
                .subscribe(System.out::println);

    }
}
