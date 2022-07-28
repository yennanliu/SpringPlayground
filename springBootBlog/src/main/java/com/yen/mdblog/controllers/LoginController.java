package com.yen.mdblog.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "redirect:/login_success";
    }

    @GetMapping("/login_success")
    public String login_success(){
        return "login_success";
    }

}
