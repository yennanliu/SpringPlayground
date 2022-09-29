package com.yen.springUserSystem.service.impl;

// Book p. 2-23. 2-24

import com.yen.springUserSystem.bean.UserSmsCode;
import com.yen.springUserSystem.bean.Vo.GetSmsCodeReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileResVo;
import com.yen.springUserSystem.bean.Vo.LoginExitReqVo;
import com.yen.springUserSystem.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo) {
        // random generate 6 digit sms verification code
        String smsCode = String.valueOf(Math.random() * 10000 + 1);
        // in read world, we HAVE to call another sms verification service
        UserSmsCode userSmsCode = UserSmsCode
                .builder()
                .smsCode(smsCode)
                .sendTime(new Timestamp(new Date().getTime()))
                .createTime(new Timestamp(new Date().getTime()))
                .build();
        // TODO : implement usersmsCodeDao
        //usersmsCodeDao.insert(userSmsCode);
        return true;
    }

    @Override
    public LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) {
        return null;
    }

    @Override
    public boolean loginExit(LoginExitReqVo loginExitReqVo) {
        return false;
    }

}
