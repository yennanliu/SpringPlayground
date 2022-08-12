package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=_5a7CjR-XSw&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=22

import com.yen.springMybatisDemo1.bean.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestParameterMapper {

    @Autowired
    ParameterMapper parameterMapper;

    @Test
    public void test1(){

        System.out.println("================");

        List<MyUser> user_list = parameterMapper.getAllUser();
        user_list.forEach(System.out::println);
    }
}
