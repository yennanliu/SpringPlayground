package com.yen.service;

// https://kucw.github.io/blog/2020/2/spring-unit-test-mockito/

import com.yen.bean.User;
import com.yen.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: implement its impl
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUserByName(String name) {

        return userMapper.getUserByName(name);
    }

    public void insertUser(User user) {

        userMapper.insertUser(user);
    }

    public List<User> getAllUser() {

        return userMapper.getAllUser();
    }
}
