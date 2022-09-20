package com.yen.mdblog.controller;

import com.yen.mdblog.entity.request.LoginRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@Log4j2
public class RegisterController {

    @GetMapping()
    public String register(Model model){
        return "register";
    }

}
