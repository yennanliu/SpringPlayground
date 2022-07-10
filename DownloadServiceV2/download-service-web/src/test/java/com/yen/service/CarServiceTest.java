package com.yen.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
     *      -> ref :
     *          https://zendei.com/article/94778.html
     *          https://developer.aliyun.com/article/240740
     */
    @Test
    public void test1(){

        int startPage = 1;
        int pageSize = 1;

        System.out.println(">>> startPage = " + startPage);
        System.out.println(">>> pageSize = " + pageSize);

        PageHelper.startPage(startPage, pageSize);
        //PageHelper.orderBy("id ASC");

        List<User> allUser = userService.getAllUser();
        System.out.println(">>> allUser = " + allUser);

        PageInfo<User> pageInfo = new PageInfo<User>(allUser);

        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        int pagesize = pageInfo.getPageSize();

        System.out.println(">>> total = " + total);
        System.out.println(">>> pages = " + pages);
        System.out.println(">>> pagesize = " + pagesize);
        System.out.println(">>> pageInfo.getNextPage() = " + pageInfo.getNextPage());



    }
}
