package com.xiaoze.springcloud.service.impl;

import com.xiaoze.springcloud.dao.UserDao;
import com.xiaoze.springcloud.entity.User;
import com.xiaoze.springcloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User get(String userNo) {

        User user = null;
        user=userDao.get(userNo);
        return user;

    }


}