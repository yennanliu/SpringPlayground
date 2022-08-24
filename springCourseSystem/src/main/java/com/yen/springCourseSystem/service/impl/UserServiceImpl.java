package com.yen.springCourseSystem.service.impl;

// book p. 250

import com.yen.springCourseSystem.bean.User;
import com.yen.springCourseSystem.mapper.UserMapper;
import com.yen.springCourseSystem.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> loadUserByUserName(String userName) {

        List<User> users = null;
        User user = userMapper.loadUserByUserName(userName);
        users = userMapper.select(user);
        return users;
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

}
