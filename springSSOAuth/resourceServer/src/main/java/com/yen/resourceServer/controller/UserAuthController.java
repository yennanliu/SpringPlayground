package com.yen.resourceServer.controller;

// book p.3-50
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/controller/UserAuthController.java

import com.yen.resourceServer.bean.bo.CheckPassWordBO;
import com.yen.resourceServer.bean.dto.CheckPassWordDTO;
import com.yen.resourceServer.entity.ResponseResult;
import com.yen.resourceServer.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/checkPassWord")
    public ResponseResult<CheckPassWordBO> checkPassword(@RequestBody @Validated CheckPassWordDTO checkPassWordDTO){

        return ResponseResult.OK(userAuthService.checkPassWord(checkPassWordDTO));
    }

}
