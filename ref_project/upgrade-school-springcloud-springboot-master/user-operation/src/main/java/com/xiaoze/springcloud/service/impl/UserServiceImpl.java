package com.xiaoze.springcloud.service.impl;

import com.xiaoze.springcloud.entity.User;
import com.xiaoze.springcloud.mapper.UserMapper;
import com.xiaoze.springcloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserServiceImpl
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper ;

    @Override
    public User get(String userNo) {
        return userMapper.selectByPrimaryKey(userNo);
    }

}