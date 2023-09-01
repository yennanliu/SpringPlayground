package com.yen.SpringBootPart1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@RestController
public class AuthController {

    @GetMapping("/public-api")
    public String publicApi() {
        return "this is a public api";
    }

}

