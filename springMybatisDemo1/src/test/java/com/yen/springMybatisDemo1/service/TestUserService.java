package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
public class TestUserService {

    @Autowired
    UserService userService;

    @Test
    public void test1(){

        User u1 = userService.getOneUser(1);
        System.out.println(">>> u1 = " + u1.toString());

        List<User> users = userService.getAllUser();
        System.out.println(">>> users = " + users.toString());

    }

}
