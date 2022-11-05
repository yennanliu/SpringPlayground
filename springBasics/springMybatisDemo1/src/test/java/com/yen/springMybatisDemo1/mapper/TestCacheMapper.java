package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=ozomvpRfzSU&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=57
// https://www.youtube.com/watch?v=kvf8-YwsuAc&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=57
// https://www.youtube.com/watch?v=jlr8Rc77boA&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=58

import com.yen.springMybatisDemo1.bean.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCacheMapper {

    @Autowired
    CacheMapper cacheMapper;

    /**  Level 1 cache : demo 1
     *
     *      -> level 1 cache : SqlSession level
     */
    @Test
    public void test1(){

        Emp emp1 = cacheMapper.getEmpByEid(1);
        System.out.println("emp1 = " + emp1);

        Emp emp2 = cacheMapper.getEmpByEid(1);
        System.out.println("emp2 = " + emp2);

        Emp emp3 = cacheMapper.getEmpByEid(1);
        System.out.println("emp3 = " + emp3);
    }

    /**  Level 1 cache : demo 2
     *
     *      ->  Level 1 cache will NOT work if there are INSERT, UPDATE, DELETE op
     */
    @Test
    public void test2(){

        Emp emp1 = cacheMapper.getEmpByEid(1);
        System.out.println("emp1 = " + emp1);

        cacheMapper.insertEmp(new Emp());

        Emp emp2 = cacheMapper.getEmpByEid(1);
        System.out.println("emp2 = " + emp2);
    }

    /**  Level 2 cache : demo 1
     *
     *      ->
     *
     *      https://www.youtube.com/watch?v=jlr8Rc77boA&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=58
     */
    @Test
    public void test3(){

        // have to use SqlSession, SqlSessionFactory
        // TODO : check if spring boot can do above
    }

}
