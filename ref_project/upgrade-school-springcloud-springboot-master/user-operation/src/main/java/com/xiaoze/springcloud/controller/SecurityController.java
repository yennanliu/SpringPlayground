package com.xiaoze.springcloud.controller;

import com.xiaoze.springcloud.annotation.CurrentUser;
import com.xiaoze.springcloud.entity.User;
import com.xiaoze.springcloud.entity.UserInfo;
import com.xiaoze.springcloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SecurityController
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */

@Slf4j
@RestController
public class SecurityController {

    @Autowired
    private UserService userService ;

    @GetMapping("/getOneUser/{userNo}")
    public User getOneUser(@PathVariable("userNo") String userNo,
                           @CurrentUser UserInfo userInfo){

        //userInfo是用户登录信息
        log.info(userInfo.toString());

        return userService.get(userNo);
    }

}