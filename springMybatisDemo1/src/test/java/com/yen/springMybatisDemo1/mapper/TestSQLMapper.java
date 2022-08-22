package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=sgmK0NNNQdE&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=35

import com.yen.springMybatisDemo1.bean.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestSQLMapper {

    @Autowired
    SQLMapper sqlMapper;

    @Test
    public void test1(){

        List<MyUser> res1 = sqlMapper.getUserByLike("z");
        System.out.println(">>> res1 = " + res1);
        System.out.println(">>> res1.get(0) = " + res1.get(0));
    }

}
