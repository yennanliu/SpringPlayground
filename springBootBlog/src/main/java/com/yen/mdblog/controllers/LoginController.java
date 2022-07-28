package com.yen.mdblog.controllers;

import com.yen.mdblog.entities.User;
import lombok.extern.log4j.Log4j2;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
public class LoginController {

    @GetMapping("/login")
    public String login(User user){

        // check login account, pwd
        if (StringUtils.hasLength(user.getUserName()) && "123".equals(user.getPassWord())){
            return "redirect:/login_success";
        }else{
            log.info(">>> login failed, plz try again ...");
            return "login";
        }
    }

    @GetMapping("/login_success")
    public String login_success(){
        return "login_success";
    }

}
