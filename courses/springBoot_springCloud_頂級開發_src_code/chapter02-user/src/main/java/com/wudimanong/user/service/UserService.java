package com.wudimanong.user.service;

import com.wudimanong.user.entity.BizException;
import com.wudimanong.user.entity.GetSmsCodeReqVo;
import com.wudimanong.user.entity.LoginByMobileReqVo;
import com.wudimanong.user.entity.LoginByMobileResVo;
import com.wudimanong.user.entity.LoginExitReqVo;

/**
 * @author joe
 */
public interface UserService {

    //获取短信验证码
    boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo);

    //短信登录
    LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws BizException;

    //登录退出
    boolean loginExit(LoginExitReqVo loginExitReqVo);

}
