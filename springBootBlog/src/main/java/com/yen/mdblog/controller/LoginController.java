package com.yen.mdblog.controller;

import com.yen.mdblog.entity.User;

import com.yen.mdblog.entity.request.LoginRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Log4j2
public class LoginController {

    @GetMapping(value = {"/login"})
    public String loginInit(Model model){

        // return LoginRequest instance as placeholder, so login html can use it and pass var to login post method (as below)
        model.addAttribute("LoginRequest", new LoginRequest());

        return "login";
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public String login(LoginRequest request){

        log.info(">>> login start ...");
        log.info(">>> loginRequest = " + request);

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassWord(request.getPassWord());

        // check login account, pwd
        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassWord())){
            //return "redirect:/login";
            return "login_success";
        }else{
            log.info(">>> login failed, plz try again ...");
            return "redirect:/login";
        }
    }

    @GetMapping("/login_success")
    public String login_success(){

        return "login_success";
    }

}
