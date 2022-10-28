package com.yen.springCourseSystem.controller;

// book p. 257
// https://github.com/yennanliu/SpringPlayground/blob/main/ref_project/easy-springboot-master/src/main/java/com/xiaoze/course/controller/SecurityController.java

import com.yen.springCourseSystem.annotation.CurrentUser;
import com.yen.springCourseSystem.bean.User;
import com.yen.springCourseSystem.bean.UserInfo;
import com.yen.springCourseSystem.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/security")
@Log4j2
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

    @ResponseBody
    @PostMapping(value="/login")
    public Boolean login(@RequestBody User user, Map<String, Object> map,
                         @CurrentUser UserInfo userInfo){

        //userInfo : for user login info
        log.info(">>> user = " + user);
        log.info(">>> userInfo.toString() : {}", userInfo.toString());
        //log.info(">>> userService.getById(user.getUserNo()) = {}", userService.getById(user.getUserNo()));

        // TODO : fix below
//        if(userService.getById(user.getUserNo()) != null){
//            User user1=userService.getById(user.getUserNo());
//            if(user1.getUserPwd().equals(user.getUserPwd())){
//                map.put("user",user1);
//                return true;
//            }
//        }
        map.put("user", user);
        return true;

        //return false;
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

        log.info(">>> username = {}, password = {}, password2 = {}", username, password, password2);

        if (password.equals(password2)){
            User userEntity = new User();
            userEntity.setUserName(username);
            userEntity.setUserPwd(password);
            userService.save(userEntity);
            log.info(">>> login OK");
            return "login";
            //return "test";
        }else{
            log.info(">>> login failed, plz try again");
            return "register";
        }
    }

    @GetMapping("/mainController/{userNo}")
    public String main(@PathVariable String userNo, Map<String, Object> map){

        log.info(">>> mainController, userNo = {}, map = {}", userNo, map);
        map.put("user", userService.getById(userNo));
        return "main";
    }

    // NOTE !! we use redirect here
    @GetMapping("/logout")
    public String logout(){

        log.info(">>> logout");
        return "redirect:/security/toLogin";
    }

}
