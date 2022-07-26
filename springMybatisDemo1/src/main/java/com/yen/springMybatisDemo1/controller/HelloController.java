package com.yen.springMybatisDemo1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller
public class HelloController {

    @GetMapping("/test")
    public String hello(){
        return "hello !!!";
    }

}
