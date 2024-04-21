package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignInDto;
import com.yen.ShoppingCart.model.dto.user.SignInResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.repository.UserRepository;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller for signIn/singUp */
// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/controller/UserController.java

@Slf4j
@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*") // TODO : check if can do CORS setting in config
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {

        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {

        return userService.signUp(signupDto);
    }

    //TODO token should be updated
    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {

        return userService.signIn(signInDto);
    }

    @GetMapping("/userProfile")
    public User getUserProfile(@RequestParam("token") String token){

        log.info("(getUserProfile) token = " + token);
        // get user from token
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        return user;
    }

    // TODO : fix/check below
    @PostMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody User user) {

        //authenticationService.authenticate(token);
        userService.updateUser(user);
        return new ResponseEntity<>(new ApiResponse(true, "user is update"), HttpStatus.OK);
    }


//    @PostMapping("/createUser")
//    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserCreateDto userCreateDto)
//            throws CustomException, AuthenticationFailException {
//        authenticationService.authenticate(token);
//        return userService.createUser(token, userCreateDto);
//    }

}
