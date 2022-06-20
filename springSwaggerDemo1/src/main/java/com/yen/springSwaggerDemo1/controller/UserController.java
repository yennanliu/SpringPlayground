package com.yen.springSwaggerDemo1.controller;

import com.yen.springSwaggerDemo1.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(
            summary = "add new user",
            description = "add user via POST, param : id, name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Add user info successfully"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Only authenticated user can add user.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user doesn't exist.",
                            content = @Content)
            }
    )
    @PostMapping("/user/add")
    public void addUser(@RequestParam("id") int id,
                        @RequestParam("name") String name){
        User user = new User(id, name);
        System.out.println(">>> add user : " + user);
    }

}
