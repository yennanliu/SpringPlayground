package com.yen.SpringAssignmentSystem.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/** Test controller */

@RestController
@Log4j2
public class TestController {

    @GetMapping("/test")
    public String index(){

        return "hello";
    }

}
