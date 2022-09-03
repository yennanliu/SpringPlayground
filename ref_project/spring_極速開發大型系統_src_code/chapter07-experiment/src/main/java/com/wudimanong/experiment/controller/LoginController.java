package com.wudimanong.experiment.controller;

import com.wudimanong.experiment.client.entity.ResponseResult;
import com.wudimanong.experiment.entity.User;
import com.wudimanong.experiment.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 * @desc 登录用户管理相关接口
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService LoginServiceImpl;

    /**
     * 登录接口
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseResult<User> login(@RequestBody User user) {
        return ResponseResult.OK(LoginServiceImpl.login(user));
    }
}
