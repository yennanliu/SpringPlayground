package com.yen.springCourseSystem.controller;

// book p. 257

import com.yen.springCourseSystem.bean.User;
import com.yen.springCourseSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String root(){
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin(Map<String, Object> map){
        map.put("user", new User());
        return "login";
    }

    // register page
    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    // register doc page
    @RequestMapping("/readdoc")
    public String readdoc(){
        return "readdoc";
    }

    // register method
    @RequestMapping("addregister")
    public String register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        if (password.equals(password2)){
            User userEntity = new User();
            userEntity.setUserName(username);
            userEntity.setUserPwd(password);
            userService.addUser(userEntity);
            return "login";
        }else{
            return "register";
        }
    }

    @GetMapping("mainController")
    public String main(){
        return "main";
    }

    // NOTE !! we use redirect here
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/security/toLogin";
    }

}
