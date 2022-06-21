package com.yen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/test")
    public String hello(){
        System.out.println(">>> hello");
        return "hello";
    }


}
