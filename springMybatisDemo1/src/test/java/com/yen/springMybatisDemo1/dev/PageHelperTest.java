package com.yen.springMybatisDemo1.dev;

// https://www.youtube.com/watch?v=1njY-J-jcq0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=66
// https://www.youtube.com/watch?v=qqoH8HIlgvo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=66

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.springMybatisDemo1.bean.Emp;
import com.yen.springMybatisDemo1.mapper.EmpMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 *  Paging demo 1
 *
 *   1) limit, index, pageSize, pageNum
 *   2) index : current page index
 *   3) pageSize : records shown by every page
 *   4) pageNum : current page
 *   5) -> index = (pageNum - 1) * pageSize
 *
 *   Using steps:
 *
 *      step 1) start page before query : PageHelper.startPage(pageNum, pageSize);
 *      step 2) run query, and get paging info (PageInfo)
 *          - e.g. : PageInfo<Emp> page = new PageInfo<>(emps, pageSize);
 *
 */
@SpringBootTest
public class PageHelperTest {

    @Autowired
    EmpMapper empMapper;

    /** pageHelper demo 1 : paging method 1 */
    @Test
    public void test1(){
        List<Emp> emps = empMapper.getAllEmp();
        // page helper
        // TODO : fix  mybatis-config.yml, so below pageHelper works
        int pageNum = 1;
        int pageSize = 4;
        Page<Object> res = PageHelper.startPage(pageNum, pageSize);
        System.out.println(">>> res = " + res);
        emps.forEach(x -> System.out.println(x));
    }

    /** pageHelper demo 2 : paging method 2 */
    @Test
    public void test2(){
        List<Emp> emps = empMapper.getAllEmp();
        // page helper
        // TODO : fix  mybatis-config.yml, so below pageHelper works
        int pageNum = 1;
        int pageSize = 4;
        //emps.forEach(x -> System.out.println(x));
        PageInfo<Emp> page = new PageInfo<>(emps, pageSize);
        System.out.println(">>> page = " + page);
    }

}
