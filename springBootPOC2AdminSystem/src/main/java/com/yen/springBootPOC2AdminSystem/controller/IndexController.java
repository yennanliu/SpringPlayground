package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=O8WUR5aSt8U&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// NOTE !!! don't forget @Controller annotation for a controller
@Controller
public class IndexController {

    /**
     *  request to endpoint = "/" or "/login"
     *  will be passed to login.html
     */
    @GetMapping(value = {"/", "/login"})
    public String loginPage(){
        return "login";
    }
}
