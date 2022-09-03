package com.wudimanong.user.service.impl;

import com.wudimanong.user.dao.UserInfoDao;
import com.wudimanong.user.dao.UserSmsCodeDao;
import com.wudimanong.user.entity.BizException;
import com.wudimanong.user.entity.GetSmsCodeReqVo;
import com.wudimanong.user.entity.LoginByMobileReqVo;
import com.wudimanong.user.entity.LoginByMobileResVo;
import com.wudimanong.user.entity.LoginExitReqVo;
import com.wudimanong.user.entity.UserInfo;
import com.wudimanong.user.entity.UserSmsCode;
import com.wudimanong.user.service.UserService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author joe
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserSmsCodeDao userSmsCodeDao;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo) {
        //随机生成6位短信验证码
        String smsCode = String.valueOf((int) (Math.random() * 100000 + 1));
        //todo 调用短信平台接口
        //存储用户短信验证码信息至短信验证码信息表
        UserSmsCode userSmsCode = UserSmsCode.builder().mobileNo(getSmsCodeReqVo.getMobileNo()).smsCode(smsCode)
                .sendTime(new Timestamp(new Date().getTime())).createTime(new Timestamp(new Date().getTime()))
                .build();
        userSmsCodeDao.insert(userSmsCode);
        return true;
    }

    @Override
    public LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws BizException {
        //1、验证短信验证码是否正确
        UserSmsCode userSmsCode = userSmsCodeDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userSmsCode == null) {
            throw new BizException(-1, "验证码输入错误");
        } else if (!userSmsCode.getSmsCode().equals(loginByMobileReqVo.getSmsCode())) {
            throw new BizException(-1, "验证码输入错误");
        }
        //2、判断用户是否注册
        UserInfo userInfo = userInfoDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userInfo == null) {
            //随机生成用户ID
            String userId = String.valueOf((int) (Math.random() * 100000 + 1));
            userInfo = UserInfo.builder().userId(userId).mobileNo(loginByMobileReqVo.getMobileNo()).isLogin("1")
                    .loginTime(new Timestamp(new Date().getTime())).createTime(new Timestamp(new Date().getTime()))
                    .build();
            //完成系统默认注册流程
            userInfoDao.insert(userInfo);
        } else {
            userInfo.setIsLogin("1");
            userInfo.setLoginTime(new Timestamp(new Date().getTime()));
            userInfoDao.updateById(userInfo);
        }
        //3、生成用户会话信息
        String accessToken = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        //将用户会话信息存储至Redis服务
        redisTemplate.opsForValue().set("accessToken", userInfo, 30, TimeUnit.DAYS);
        //4、封装响应参数
        LoginByMobileResVo loginByMobileResVo = LoginByMobileResVo.builder().userId(userInfo.getUserId())
                .accessToken(accessToken).build();
        return loginByMobileResVo;
    }

    @Override
    public boolean loginExit(LoginExitReqVo loginExitReqVo) {
        try {
            redisTemplate.delete(loginExitReqVo.getAccessToken());
            return true;
        } catch (Exception e) {
            log.error(e.toString() + "_" + e);
            return false;
        }
    }
}
