package com.yen.service;

import com.github.pagehelper.PageHelper;
import com.yen.bean.User;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarServiceTest {

    @Autowired
    UserService userService;


    /**
     *   mybatis PageHelper test 1
     *
     *      -> ref : https://zendei.com/article/94778.html
     */
    @Test
    public void test1(){

        int startPage= 2;
        int pageSize= 2;

        PageHelper.startPage(startPage, pageSize);
        //PageHelper.orderBy("id ASC");

        List<User> allUser = userService.getAllUser();

        System.out.println(">>> allUser = " + allUser);
    }
}
