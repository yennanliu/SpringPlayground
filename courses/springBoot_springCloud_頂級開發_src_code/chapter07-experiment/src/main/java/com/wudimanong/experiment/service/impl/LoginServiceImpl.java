package com.wudimanong.experiment.service.impl;

import com.wudimanong.experiment.client.entity.BusinessCodeEnum;
import com.wudimanong.experiment.entity.User;
import com.wudimanong.experiment.exception.ServiceException;
import com.wudimanong.experiment.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public User login(User user) {
        //为方便测试，这里写死，正式系统可扩展用户登录逻辑
        if (user.getUsername().equals("admin") && user.getPassword().equals("123456")) {
            user.setId(123);
            user.setName("无敌码农");
            user.setPassword("");
            //通过github存储小头像
            user.setAvatar("https://github.com/manongwudi/repos/blob/master/static/images/avator-wudimanong.jpg");
        } else {
            throw new ServiceException(BusinessCodeEnum.BUSI_LOGIN_FAIL_1000.getCode(),
                    BusinessCodeEnum.BUSI_LOGIN_FAIL_1000.getDesc());
        }
        return user;
    }
}
