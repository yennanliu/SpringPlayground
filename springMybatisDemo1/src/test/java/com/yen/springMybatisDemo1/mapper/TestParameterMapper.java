package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=_5a7CjR-XSw&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=22
// https://www.youtube.com/watch?v=eesVBIuTC1k&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=23
// https://www.youtube.com/watch?v=4oaMxuXRV0U&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=25
// https://www.youtube.com/watch?v=4oaMxuXRV0U&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=25
// https://www.youtube.com/watch?v=kmPPvKs0G6Y&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=26

import com.yen.springMybatisDemo1.bean.MyUser;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.List;

/**
 *    Mybatis has 2 ways get parameter (to SQL)
 *
 *       1) ${} : concatenation of strings (字符串拼接)
 *              -> has SQL injection issue
 *              -> have to use this in some scenario
 *
 *       2) #{} : replacement (佔位符賦值)
 *              -> prefer this
 *              -> has NO SQL injection issue
 *
 *       3) example cases:
 *
 *          -> 3-1) mapper interface has single parameter (name)
 *              -> via #{}
 *              -> via "'" + ${} + "'"
 *
 *          -> 3-2) mapper interface has MULTIPLE parameters (multiple format)
 *              -> Mybatis will put those params in a map (k-v) collection
 *              -> can access them via 2 ways
 *                  -> arg0, arg1, ... as "key"
 *                  -> param1, param2, ...  as "value"
 *
 *                  -> example : #{arg0}, #{arg1}...
 */

@SpringBootTest
public class TestParameterMapper {

    @Autowired
    ParameterMapper parameterMapper;

    @Test
    public void test1(){

        List<MyUser> user_list = parameterMapper.getAllUser();
        user_list.forEach(System.out::println);
    }

    @Test
    public void test2(){

        MyUser u1 = parameterMapper.getUserByName("lynn");
        System.out.println(">>> u1 = " + u1);
    }

    @Test
    public void test3(){

        MyUser u1 = parameterMapper.checkLogin("amy", "123");
        System.out.println(">>> u1 = " + u1);
    }

    /** review : tradition JDBC op */
    @Test
    public void testJDBC() throws Exception {

        String url = "jdbc:mysql://localhost:3306/mybatis";
        String user = "root";
        String password = "";

        String sql1 = "SELECT * FROM my_user WHERE id = ?";
        String sql2 = "SELECT * FROM my_user WHERE id = {}";

        Class.forName("");
        Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = connection.prepareStatement(sql1);
        ps.setString(1, "1");
    }

}
