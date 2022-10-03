package com.yen.springUserSystem.service.impl;

// Book p. 2-23. 2-24, 2-25

import com.yen.springUserSystem.bean.UserInfo;
import com.yen.springUserSystem.bean.UserSmsCode;
import com.yen.springUserSystem.bean.Vo.GetSmsCodeReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileReqVo;
import com.yen.springUserSystem.bean.Vo.LoginByMobileResVo;
import com.yen.springUserSystem.bean.Vo.LoginExitReqVo;
import com.yen.springUserSystem.mapper.UserInfoDao;
import com.yen.springUserSystem.mapper.UserSmsCodeDao;
import com.yen.springUserSystem.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserSmsCodeDao userSmsCodeDao;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    RedisTemplate redisTemplate;

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

        userSmsCodeDao.insert(userSmsCode);
        return true;
    }

    @Override
    public LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws Exception{

        UserSmsCode userSmsCode = userSmsCodeDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());

        // 1) check if verification is correct
        if (userSmsCode == null){
            // TODO : implement BizException
            throw new Exception("verification code input error");
        }else if (!userSmsCode.getSmsCode().equals(loginByMobileReqVo.getSmsCode())){
            throw new Exception("verification code input error");
        }

        // 2) check if user already registered
        UserInfo userInfo = userInfoDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userInfo == null){
            // random generate user id
            String userId = String.valueOf((int) (Math.random() * 10000 + 1));
            userInfo = UserInfo
                    .builder()
                    .userId(userId)
                    .build();

            // finish system default process
            userInfoDao.insert(userInfo);
        }else {
            userInfo.setIsLogin(Boolean.valueOf("1"));
            userInfo.setLoginTime(new Timestamp(new Date().getTime()));
            userInfoDao.updateById(userInfo);
        }

        // 3) create user
        String accessToken = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        // save user info to redis
        redisTemplate.opsForValue().set("accessToken", userInfo, 30, TimeUnit.DAYS);
        // 4) encrypt param
        LoginByMobileResVo loginByMobileResVo = LoginByMobileResVo
                .builder()
                .userId(userInfo.getUserId())
                .accessToken(accessToken)
                .build();

        return loginByMobileResVo;
    }

    @Override
    public boolean loginExit(LoginExitReqVo loginExitReqVo) {

        try{
            redisTemplate.delete(loginExitReqVo.getAccessToken());
            log.info(">>> loginExit OK");
            return true;
        }catch (Exception e){
            String errorMsg = ">>> loginExit failed ..." + e.toString() + "_" + e;
            log.error(errorMsg);
            return false;
        }
    }

}
