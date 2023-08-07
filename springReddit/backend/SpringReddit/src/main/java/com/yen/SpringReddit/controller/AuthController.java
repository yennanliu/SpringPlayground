package com.yen.SpringReddit.controller;

// https://youtu.be/kpKUMmAmcj0?t=123

import com.yen.SpringReddit.dto.RegisterRequest;
import com.yen.SpringReddit.po.User;
import com.yen.SpringReddit.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){

        authService.signUp(registerRequest);
        return new ResponseEntity<>("User register success", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){

        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activation success", HttpStatus.OK);
    }

}
