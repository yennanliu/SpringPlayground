﻿package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: Test
 * Author:   longzhonghua
 * Date:     2019/4/28 13:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class propertTest {
    //取得組態檔中的age
    @Value("${age}")
    private int age;

    //取得組態檔中的name
    @Value("${name}")
    private String name;

    @Test
    public void getAge() {
        System.out.println(age);
    }

    @Test
    public void getName() {
        System.out.println(name);
    }

    @Autowired
    private GetPersonInfoProperties getPersonInfoProperties;
    @Test
    public void getPersonproperties() {
        System.out.println(getPersonInfoProperties.getName()+getPersonInfoProperties.getAge());
    }
}