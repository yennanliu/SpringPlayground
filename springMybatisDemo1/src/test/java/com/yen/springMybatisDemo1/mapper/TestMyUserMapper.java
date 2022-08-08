package com.yen.springMybatisDemo1.mapper;

import com.yen.springMybatisDemo1.bean.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMyUserMapper {

    @Autowired
    MyUserMapper myUserMapper;

    @Test
    public void test1(){

        myUserMapper.insertUser(new MyUser("ll","123",10,"0","yoh@google.com"));
        myUserMapper.insertUser(new MyUser("kk","123",10,"0","yoh@google.com"));
    }

}
