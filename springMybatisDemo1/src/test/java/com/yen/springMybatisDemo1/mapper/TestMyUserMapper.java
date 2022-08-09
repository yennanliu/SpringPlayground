package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=DACvS6eOiGI&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=12
// https://www.youtube.com/watch?v=EYMDtHRLyCM&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=13

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

        int res1 = myUserMapper.insertUser(new MyUser("ll","123",10,"0","yoh@google.com"));
        int res2 = myUserMapper.insertUser(new MyUser("kk","123",10,"0","yoh@google.com"));

        System.out.println(">>> res1 = " + res1);
        System.out.println(">>> res2 = " + res2);
    }

    @Test
    public void test2(){

        // via sqlSession
        // https://www.youtube.com/watch?v=DACvS6eOiGI&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=12
    }

}
