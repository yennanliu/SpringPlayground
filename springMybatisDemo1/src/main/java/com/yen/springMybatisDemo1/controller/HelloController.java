package com.yen.springMybatisDemo1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
public class HelloController {

    @GetMapping("/test")
    public String hello(){
        return "hello !!!";
    }

}
