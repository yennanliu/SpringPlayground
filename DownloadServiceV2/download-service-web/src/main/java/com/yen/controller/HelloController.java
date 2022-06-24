package com.yen.controller;

import com.yen.bean.Admin;
import com.yen.bean.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/test")
    public String hello(){
        System.out.println(">>> hello");
        return "hello";
    }

    @PostMapping("/test2")
    public User hello2(User user){
        System.out.println(">>> user = " + user);
        return user;
    }

    @PostMapping("/test3")
    public Admin hello3(@RequestBody Admin admin){
        System.out.println(">>> admin.getParam() = " + admin.getParam());
        System.out.println(">>> admin = " + admin);
        return admin;
    }


}
