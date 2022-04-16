package com.yen.SpringBootPOC1.controller;

// https://www.youtube.com/watch?v=N7dsPcsmxM4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=6

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// either use @ResponseBody + @Controller, or use  @RestController directly
//@ResponseBody
//@Controller
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle1(){
        return "hello !!! spring boot 2 !!";
    }
}
