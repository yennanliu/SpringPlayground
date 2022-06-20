package com.yen.springSwaggerDemo1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.swing.plaf.PanelUI;

@RestController
public class UserController {

    @GetMapping("/user/test")
    public String hello(){
        System.out.println(">>> hello user !!!");
        return "hello user !!!";
    }

    @PostMapping("/user/add")
    public void addUser()



}
