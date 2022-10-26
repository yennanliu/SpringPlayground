package com.xiaoze.springcloud.controller;

import java.util.Map;

import com.xiaoze.springcloud.annotation.CurrentUser;
import com.xiaoze.springcloud.entity.User;
import com.xiaoze.springcloud.entity.UserInfo;
import com.xiaoze.springcloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * SecurityController
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Slf4j
@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private UserService userService ;

    @RequestMapping("/index")
    public String root() {
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin(Map<String, Object> map) {

        map.put("user", new User());

        return "login";
    }

    @ResponseBody
    @PostMapping(value="/login")
    public Boolean login(@RequestBody User user, Map<String, Object> map,
                         @CurrentUser UserInfo userInfo){

        //userInfo是用户登录信息
        log.info(userInfo.toString());

        if(userService.get(user.getUserNo()) != null){
            User user1=userService.get(user.getUserNo());
            if(user1.getUserPwd().equals(user.getUserPwd())){
                map.put("user",user1);
                return true;
            }
        }

        return false;
    }

    @GetMapping("/mainController/{userNo}")
    public String main(@PathVariable String userNo, Map<String, Object> map){
        map.put("user",userService.get(userNo));
        return "main";
    }

    @GetMapping("/logout")
    public String logout(){

        return "redirect:/security/toLogin";

    }

}