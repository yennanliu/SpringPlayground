package com.yen.OAuthLoginDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
class UserController {

    @GetMapping("/user")
    public Principal getUser(Principal principal){
        System.out.println(">>> principal = " + principal.toString());
        return principal;
    }

}