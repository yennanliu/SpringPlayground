package com.yen.thymeleafDemo.conroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String hello(){
        System.out.println(">>> hello");
        return "index";
    }

}
