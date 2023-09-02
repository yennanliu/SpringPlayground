package com.yen.SpringAssignmentSystem.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/** Test controller */

@RestController
@Log4j2
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class TestController {

    @GetMapping("/test")
    public String index(){

        return "hello";
    }

    @PostMapping("/test_post")
    public String index2(HttpServletRequest request){

        System.out.println(">>> request = " + request.toString());
        return "hello";
    }

}
