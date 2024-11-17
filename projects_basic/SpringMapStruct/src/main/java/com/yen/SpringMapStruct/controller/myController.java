package com.yen.SpringMapStruct.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class myController {

    @GetMapping("/test1")
    public String test1(){
        return "OK";
    }
}
