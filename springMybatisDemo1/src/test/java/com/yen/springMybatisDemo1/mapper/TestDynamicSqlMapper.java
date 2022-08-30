package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=dNLGsANJ790&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=VqjaBphBdH4&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50

import com.yen.springMybatisDemo1.bean.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestDynamicSqlMapper {

    @Autowired
    DynamicSqlMapper dynamicSqlMapper;

    /** dynamic SQL demo
     *
     *  1) if : based on test attr, decide whether put content in <if></if> to SQL
     *
     *      e.g. :
     *         <if test="empName != null and empName != ''">
     *             AND emp_name = #{empName}
     *         </if>
     *
     *  2) where :
     *             -> when there is a content in <if></if>, will generate "where" syntax automatically,
     *             and remove redundant "and", "or" in front of it
     *
     *             -> when there is NO content, "where" syntax will not be generated (do nothing)
     *
     *  3) NOTE : where 1 = 1 trick
     *
     *      e.g. :
     *         WHERE
     *         1 = 1
     */

    /**  test multi condition dynamic SQL V1 */
    @Test
    public void test1(){

        List<Emp> res1 = dynamicSqlMapper.getEmpByCondition(new Emp("jack", 29, "1", "jack@fb.com"));
        System.out.println(res1);

        List<Emp> res2 = dynamicSqlMapper.getEmpByCondition(new Emp("jack", 29, null, "jack@fb.com"));
        System.out.println(res2);

        List<Emp> res3 = dynamicSqlMapper.getEmpByCondition(new Emp(null, 29, null, null));
        System.out.println(res3);

        List<Emp> res4 = dynamicSqlMapper.getEmpByCondition(new Emp(null, null, "1", null));
        System.out.println(res4);
    }


    /**  test multi condition dynamic SQL V2 */
    @Test
    public void test2(){

        List<Emp> res1 = dynamicSqlMapper.getEmpByCondition2(new Emp("jack", 29, "1", "jack@fb.com"));
        System.out.println(res1);

        List<Emp> res2 = dynamicSqlMapper.getEmpByCondition2(new Emp("jack", 29, null, "jack@fb.com"));
        System.out.println(res2);

        List<Emp> res3 = dynamicSqlMapper.getEmpByCondition2(new Emp(null, 29, null, null));
        System.out.println(res3);

        List<Emp> res4 = dynamicSqlMapper.getEmpByCondition2(new Emp(null, null, "1", null));
        System.out.println(res4);
    }

}
