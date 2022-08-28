package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=nvLKTBxgIdg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=46

import com.yen.springMybatisDemo1.bean.Dept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestDeptMapper {

    @Autowired
    DeptMapper deptMapper;

    @Test
    public void test1(){

        Dept res1 = deptMapper.getDeptAndEmp(1);
        System.out.println(">>> res1 = " + res1);
    }

}
