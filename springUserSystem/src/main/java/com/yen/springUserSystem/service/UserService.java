package com.yen.springUserSystem.service;

// Book p. 2-22

import com.yen.springUserSystem.bean.Vo.GetSmsCodeReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileResVo;
import com.yen.springUserSystem.bean.Vo.LoginExitReqVo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    // get SMS authentication msg
    boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo);

    // msg login
    LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo);

    // login logout
    boolean loginExit(LoginExitReqVo loginExitReqVo);

}
