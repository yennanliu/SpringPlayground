package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.User;
import com.yen.springMybatisDemo1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getOneUser(int id){
        return userMapper.select1(id);
    }

    public List<User> getAllUser(){
        return userMapper.selectAll();
    }

    public int insertOneUser(User user){
        return userMapper.insert1(user);
    }

    public int deleteOneUser(int id){
        return userMapper.delete1(id);
    }

    public int updateOneUser(User user){
        return userMapper.update1(user);
    }

}
