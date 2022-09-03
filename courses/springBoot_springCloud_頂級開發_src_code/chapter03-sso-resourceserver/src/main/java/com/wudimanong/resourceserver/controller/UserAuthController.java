package com.wudimanong.resourceserver.controller;

import com.wudimanong.resourceserver.entity.ResponseResult;
import com.wudimanong.resourceserver.entity.bo.CheckPassWordBO;
import com.wudimanong.resourceserver.entity.dto.CheckPassWordDTO;
import com.wudimanong.resourceserver.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    /**
     * 业务层依赖组件
     */
    @Autowired
    UserAuthService userAuthServiceImpl;

    /**
     * 登录密码验证接口
     *
     * @param checkPassWordDTO
     * @return
     */
    @PostMapping("/checkPassWord")
    public ResponseResult<CheckPassWordBO> checkPassWord(@RequestBody @Validated CheckPassWordDTO checkPassWordDTO) {
        return ResponseResult.OK(userAuthServiceImpl.checkPassWord(checkPassWordDTO));
    }
}
