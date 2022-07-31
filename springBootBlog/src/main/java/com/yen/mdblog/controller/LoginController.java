package com.yen.mdblog.controller;

import com.yen.mdblog.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
public class LoginController {

    @GetMapping(value = {"/login"})
    public String loginInit(Model model){

        model.addAttribute("userName", "user");
        model.addAttribute("passWord", 123);

        return "login";
    }

//    @PostMapping("/login")
//    public String login(User user){
//
//        // check login account, pwd
//        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassWord())){
//            //return "redirect:/login";
//            return "login_success";
//        }else{
//            log.info(">>> login failed, plz try again ...");
//            //return "login_success";
//            return "redirect:/login";
//        }
//    }

    @GetMapping("/login_success")
    public String login_success(){

        return "login_success";
    }

}
