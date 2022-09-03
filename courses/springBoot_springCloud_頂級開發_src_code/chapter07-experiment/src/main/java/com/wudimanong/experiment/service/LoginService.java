package com.wudimanong.experiment.service;

import com.wudimanong.experiment.entity.User;

/**
 * @author jiangqiao
 */
public interface LoginService {

    /**
     * 用户登录接口
     *
     * @param user
     * @return
     */
    User login(User user);

}
