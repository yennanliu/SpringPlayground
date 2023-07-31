package com.yen.SpringReddit.controller;

// https://youtu.be/kpKUMmAmcj0?t=123


import com.yen.SpringReddit.dto.RegisterRequest;
import com.yen.SpringReddit.po.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.BeanUtils;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/signup")
    public void signup(@RequestBody RegisterRequest registerRequest){

    }
}
