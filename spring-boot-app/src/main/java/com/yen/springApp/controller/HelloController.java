package com.yen.springApp.controller;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=84

import com.yen.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/test")
    public String sayHello(){
        String msg =  helloService.sayHello("Lynn");
        return msg;
    }

}
