package com.yen.springBootAdvance1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/test")
    public String test(){
        System.out.println(">>> hello ");
        return "word";
    }

}
