package com.yen.springThreadPool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {

    @GetMapping("/test")
    public String hello(){

        return "hello world!!!";
    }

}
