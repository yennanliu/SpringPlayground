package com.yen.springThreadPool.controller;

import com.yen.springThreadPool.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@EnableAsync
@RestController
public class helloController {

    @Autowired
    HelloService helloService;

    public static void mySleep(long time) {
        try {
            System.out.printf("sleep for %d milli\n", time);
            Thread.sleep(time);
            System.out.printf("wake up\n");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test")
    public String hello() {

        return "hello world!!!";
    }

    @GetMapping("/test2")
    public String hello2() {

        helloService.printNumMultiThread();
        return "hello2";
    }

    @GetMapping("/test3")
    public String hello3() {

        CompletableFuture.runAsync(() -> {
            try {
                //System.out.println(">>> Thread id = " + Thread.currentThread().getId() + ", Thread name = " + Thread.currentThread().getName());
                helloService.slowHello();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        return "test3";
    }

    @Async
    @GetMapping("/test4")
    public String hello4() throws InterruptedException {

        helloService.slowHello();
        return "test4";
    }

    @Async
    @GetMapping("/test5")
    public String hello5() throws InterruptedException {

        CompletableFuture.runAsync(() -> {
            try {
                helloService.slowHello();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).whenComplete((result, throwable) -> {
            System.out.println("DONE!");
        });

        return "test5";
    }

    @Async
    @GetMapping("/test6")
    public String hello6() throws InterruptedException {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> mySleep(1000)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                return;
            }
            CompletableFuture.runAsync(() -> mySleep(1000)).whenComplete((result2, throwable2) -> {
                if (throwable2 != null) {
                    return;
                }

                CompletableFuture.runAsync(() -> mySleep(1000)).whenComplete((result3, throwable3) -> {
                    if (throwable2 != null) {
                        return;
                    }

                    System.out.println("Done");
                });
            });
        });
        return "test6";
    }

}
