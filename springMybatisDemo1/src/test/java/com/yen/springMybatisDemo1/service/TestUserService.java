package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUserService {

    @Autowired
    UserService userService;

    @Test
    public void test1(){

        User u1 = userService.getOneUser(1);
        System.out.println(">>> u1 = " + u1.toString());
    }

}
