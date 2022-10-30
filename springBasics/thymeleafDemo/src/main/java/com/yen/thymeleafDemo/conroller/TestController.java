package com.yen.thymeleafDemo.conroller;

// book p.132

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping("/test1")
    public String hello(HttpServletRequest request){
        System.out.println(">>> test1");
        request.setAttribute("description", "this is description value");
        return "test1";
    }

}
