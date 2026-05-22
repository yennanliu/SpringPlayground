package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.UserService;
import com.yen.FlinkRestService.model.dto.auth.AuthResponse;
import com.yen.FlinkRestService.model.dto.auth.SigninRequest;
import com.yen.FlinkRestService.model.dto.auth.SignupRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest req) {
        log.info("Signup request for email={}", req.getEmail());
        return ResponseEntity.ok(userService.signup(req));
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SigninRequest req) {
        log.info("SignIn request for email={}", req.getEmail());
        return ResponseEntity.ok(userService.signin(req));
    }
}
