package com.yen.springBootPOC2AdminSystem.controller;

// https://www.youtube.com/watch?v=O8WUR5aSt8U&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=44

/** controller for landing page
 *
 *  - login page (login.html)
 *  - main page (main.html)
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// NOTE !!! don't forget @Controller annotation for a controller
@Controller
public class IndexController {

    /**
     *  Non - login page
     *
     *  request to endpoint = "/" or "/login"
     *  will be passed to login.html
     */
    @GetMapping(value = {"/", "/login"})
    public String loginPage(){
        return "login";
    }

    /**
     *  Already-login page
     */
    @PostMapping("/login")
    public String main(String username, String password){

        //return "main";

        /**
         *  Re-direct : avoid "resend request" to a form.
         *      NOTE !!! if login success, redirect to main.html (instead of send a new request to main.html everytime)
         */
        return "redirect:/main.html";
    }

    /**
     *  Main page
     *  
     *  so, once login success, every request will be redirected to main.html (instead of login)
     */
    @GetMapping("/main.html")
    public String mainPage(){
        return "main";
    }

}
