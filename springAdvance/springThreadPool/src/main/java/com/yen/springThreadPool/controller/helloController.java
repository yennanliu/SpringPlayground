package com.yen.springThreadPool.controller;

import com.yen.springThreadPool.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
