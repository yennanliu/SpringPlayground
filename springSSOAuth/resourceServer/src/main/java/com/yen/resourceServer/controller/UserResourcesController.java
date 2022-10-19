package com.yen.resourceServer.controller;

// book p.3-52
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/controller/UserResourcesController.java

import com.yen.resourceServer.bean.ResponseResult;
import com.yen.resourceServer.bean.bo.GetUserInfoBO;
import com.yen.resourceServer.bean.dto.GetUserInfoDTO;
import com.yen.resourceServer.service.UserResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserResourcesController {

    @Autowired
    UserResourceService userResourceService;

    @GetMapping("/getUserInfo")
    public ResponseResult<GetUserInfoBO> getUserInfo(@Validated GetUserInfoDTO getUserInfoDTO){

        return ResponseResult.OK(userResourceService.getUserInfo(getUserInfoDTO));
    }

}
