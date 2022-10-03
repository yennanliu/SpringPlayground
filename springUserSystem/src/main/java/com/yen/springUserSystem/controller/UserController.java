package com.yen.springUserSystem.controller;

// Book p.2-19, 2-20, 2-24

import com.yen.springUserSystem.bean.ApiResponse;
import com.yen.springUserSystem.bean.Vo.GetSmsCodeReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileResVo;
import com.yen.springUserSystem.bean.Vo.LoginExitReqVo;
import com.yen.springUserSystem.enums.GlobalCodeEnum;
import com.yen.springUserSystem.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "getSmsCode")
    public Boolean getSmsCode(@RequestParam("reqId") String reqId,
                              @RequestParam("mobileNo") String mobileNo){

        GetSmsCodeReqVo getSmsCodeReqVo = GetSmsCodeReqVo
                .builder()
                .reqId(reqId)
                .mobileNo(mobileNo)
                .build();

        boolean result = userService.getSmsCode(getSmsCodeReqVo);
        log.info("reqId = {}, mobileNo = {}", reqId, mobileNo);
        log.info("result = {}", result);
        return result;
    }

    @PostMapping(value = "loginByMobile")
    public ApiResponse loginByMobile(@RequestParam("reqId") String reqId,
                                     @RequestParam("mobileNo") String mobileNo,
                                     @RequestParam("smsCode") String smsCode) throws Exception {

        LoginByMobileReqVo loginByMobileReqVo = LoginByMobileReqVo
                .builder()
                .reqId(reqId)
                .mobileNo(mobileNo)
                .smsCode(smsCode)
                .build();

        LoginByMobileResVo loginByMobileResVo = userService.loginByMobile(loginByMobileReqVo);

        return ApiResponse.success(
                GlobalCodeEnum.GL_SUCC_0000.getCode().toString(),
                GlobalCodeEnum.GL_SUCC_0000.getDesc(),
                loginByMobileResVo);
    }

    @PostMapping(value = "loginExit")
    public Boolean loginExit(@RequestParam("userId") String userId,
                             @RequestParam("accessToken") String accessToken){

        LoginExitReqVo loginExitReqVo = LoginExitReqVo
                .builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();

        Boolean result = userService.loginExit(loginExitReqVo);
        return result;
    }

}
