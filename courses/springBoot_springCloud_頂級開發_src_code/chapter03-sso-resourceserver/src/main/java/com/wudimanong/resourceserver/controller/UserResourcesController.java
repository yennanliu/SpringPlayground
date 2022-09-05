package com.wudimanong.resourceserver.controller;

import com.wudimanong.resourceserver.entity.ResponseResult;
import com.wudimanong.resourceserver.entity.bo.GetUserInfoBO;
import com.wudimanong.resourceserver.entity.dto.GetUserInfoDTO;
import com.wudimanong.resourceserver.service.UserResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@RestController
@RequestMapping("/user")
public class UserResourcesController {

    /**
     * 依赖注入Service层业务组件
     */
    @Autowired
    UserResourcesService userResourcesServiceImpl;

    /**
     * 获取有限用户信息接口
     *
     * @param getUserInfoDTO
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResponseResult<GetUserInfoBO> getUserInfo(@Validated GetUserInfoDTO getUserInfoDTO) {
        return ResponseResult.OK(userResourcesServiceImpl.getUserInfo(getUserInfoDTO));
    }
}
