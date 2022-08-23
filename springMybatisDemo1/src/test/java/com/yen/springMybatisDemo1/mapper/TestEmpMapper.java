package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=D_DDE_o7ks0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39

import com.yen.springMybatisDemo1.bean.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestEmpMapper {

    @Autowired
    EmpMapper empMapper;

    /** Deal with java bean attr - SQL col mismatch case
     *
     *    solution 1) : use alias in SQL query in mapper xml
     *    solution 2) : via mybatis conf (mybatis.configuration.map-underscore-to-camel-case = true)
     *    solution 3) : via resultMap
     */
    @Test
    public void test1(){

        List<Emp> res1 = empMapper.getAllEmp();
        System.out.println(">>> res1 = " + res1);
        res1.stream().forEach(System.out::println);
    }

}
