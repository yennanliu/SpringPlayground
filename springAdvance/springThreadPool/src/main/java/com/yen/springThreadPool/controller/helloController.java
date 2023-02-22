package com.yen.springThreadPool.controller;

import com.yen.springThreadPool.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class helloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/test")
    public String hello(){

        return "hello world!!!";
    }

    @GetMapping("/test2")
    public String hello2(){

        helloService.printNumMultiThread();
        return "hello2";
    }

    @GetMapping("/test3")
    public String hello3(){

        CompletableFuture.runAsync(() -> {
            try {
                //System.out.println(">>> Thread id = " + Thread.currentThread().getId() + ", Thread name = " + Thread.currentThread().getName());
                helloService.slowHello();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return "test3";
    }

}
