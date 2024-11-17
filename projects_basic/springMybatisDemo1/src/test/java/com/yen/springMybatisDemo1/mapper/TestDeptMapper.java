package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=nvLKTBxgIdg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=46
// https://www.youtube.com/watch?v=I4obnFs_CB8&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=47

import com.yen.springMybatisDemo1.bean.Dept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestDeptMapper {

    @Autowired
    DeptMapper deptMapper;

    /** one-multiple mapping demo (via collection) */
    @Test
    public void test1(){

        Dept res1 = deptMapper.getDeptAndEmp(1);
        System.out.println(">>> res1 = " + res1);

        System.out.println("====================");

        Dept res2 = deptMapper.getDeptAndEmp(3);
        System.out.println(">>> res2 = " + res2);
    }

    /** one-multiple mapping demo (via step SQL 分步查詢) */
    @Test
    public void test2(){

        Dept res1 = deptMapper.getDeptAndEmpByStep1(1);
        System.out.println(">>> res1 = " + res1);

        System.out.println("====================");

        Dept res2 = deptMapper.getDeptAndEmpByStep1(3);
        System.out.println(">>> res2 = " + res2);
    }

}
