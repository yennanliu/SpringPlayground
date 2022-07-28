package com.yen.SpringBlog.controllers;

// https://www.roshanadhikary.com.np/2021/05/build-a-markdown-based-blog-with-spring-boot-part-2.html

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping("")
    public String index(){
        // all GET requests to the / route will be redirected to the /posts route.
        return "redirect:posts";
    }

}
