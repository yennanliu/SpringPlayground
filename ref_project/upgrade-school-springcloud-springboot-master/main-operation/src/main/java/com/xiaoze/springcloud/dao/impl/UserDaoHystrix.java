package com.xiaoze.springcloud.dao.impl;

import com.xiaoze.springcloud.dao.UserDao;
import com.xiaoze.springcloud.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author : xiaoze
 * @date : 2019/7/25
 */
@Component
public class UserDaoHystrix implements UserDao {

    @Override
    public User get(String userNo) {
        return new User();
    }

}
