package com.yen.springUserSystem.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/index")
public class indexController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

}
