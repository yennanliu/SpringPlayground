package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=dNLGsANJ790&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=VqjaBphBdH4&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=t0pYgJu_nJ0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=52
// https://www.youtube.com/watch?v=bUXDOzn1phg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=52
// https://www.youtube.com/watch?v=Be9IYx1718k&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=54
// https://www.youtube.com/watch?v=ht97kZOvYCI&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=55
// https://www.youtube.com/watch?v=QfhtOXzEGQM&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=55

import com.yen.springMybatisDemo1.bean.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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
     *  3) trim :
     *      prefix :  add defined val before specific content
     *      suffix :  add defined val after specific content
     *      suffixOverrides : remove defined val before specific content
     *      prefixOverrides : remove defined val after specific content
     *
     *      -> if there is content in <trim></trim> -> works
     *      -> if there is NO content in <trim></trim> -> do nothing
     *
     *  4) choose, when, otherwise => similar as if, elseif, else in java
     *      -> we need one "when" at least
     *      -> we can only have one "otherwise" at MOST
     *
     *  5) for-each
     *      collection : set up array/collection needed in for-loop
     *      item : basic element (data) in array/collection
     *      separator : separator between each item
     *      open : first sign before all content in for-loop
     *      close : last sign before all content in for-loop
     *
     *  7) Sql flag:
     *
     *      e.g. (setup) :
     *              <sql id="empColumns">
     *                  eid,emp_name,age,sex,email
     *              </sql>
     *
     *      e.g. (use/reference):
     *               <include refid="empColumns"></include>
     *
     *  7) NOTE : where 1 = 1 trick
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

    /**  test multi condition dynamic SQL V3 */
    @Test
    public void test3(){

        List<Emp> res1 = dynamicSqlMapper.getEmpByCondition3(new Emp("jack", 29, "1", "jack@fb.com"));
        System.out.println(res1);

        List<Emp> res2 = dynamicSqlMapper.getEmpByCondition3(new Emp("jack", 29, null, "jack@fb.com"));
        System.out.println(res2);

        List<Emp> res3 = dynamicSqlMapper.getEmpByCondition3(new Emp(null, 29, null, null));
        System.out.println(res3);

        List<Emp> res4 = dynamicSqlMapper.getEmpByCondition3(new Emp(null, null, "1", null));
        System.out.println(res4);
    }

    /**  test multi condition dynamic SQL V4 (with SQL flag) */
    @Test
    public void test3_1(){

        List<Emp> res1 = dynamicSqlMapper.getEmpByCondition4(new Emp("jack", 29, "1", "jack@fb.com"));
        System.out.println(res1);

        List<Emp> res2 = dynamicSqlMapper.getEmpByCondition4(new Emp("jack", 29, null, "jack@fb.com"));
        System.out.println(res2);

        List<Emp> res3 = dynamicSqlMapper.getEmpByCondition4(new Emp(null, 29, null, null));
        System.out.println(res3);

        List<Emp> res4 = dynamicSqlMapper.getEmpByCondition4(new Emp(null, null, "1", null));
        System.out.println(res4);
    }

    /**  test getEmpByChoose */
    @Test
    public void test4(){

        List<Emp> res1 = dynamicSqlMapper.getEmpByChoose(new Emp("jack", 29, "1", "jack@fb.com"));
        System.out.println(res1);

        List<Emp> res2 = dynamicSqlMapper.getEmpByChoose(new Emp("jack", 29, null, "jack@fb.com"));
        System.out.println(res2);

        List<Emp> res3 = dynamicSqlMapper.getEmpByChoose(new Emp(null, 29, null, null));
        System.out.println(res3);

        List<Emp> res4 = dynamicSqlMapper.getEmpByChoose(new Emp(null, null, null, null));
        System.out.println(res4);
    }

    /** test batch delete (for-each) */
    @Test
    public void test5(){
        Integer[] eids = new Integer[]{1,2,3};
        int res1 = dynamicSqlMapper.deleteMultiEmpByArray(eids);
        System.out.println(res1);
    }

    /** test batch delete V2 (for-each) */
    @Test
    public void test6(){
        Integer[] eids = new Integer[]{1,2,3};
        int res1 = dynamicSqlMapper.deleteMultiEmpByArray2(eids);
        System.out.println(res1);
    }

    /** test batch delete V3 (for-each + or) */
    @Test
    public void test7(){
        Integer[] eids = new Integer[]{1,2,3};
        int res1 = dynamicSqlMapper.deleteMultiEmpByArray3(eids);
        System.out.println(res1);
    }

    /** test batch insert (for-each) */
    @Test
    public void test8(){
        Emp e1 = new Emp();
        Emp e2 = new Emp("lily",20,"0","lily@fb.com");
        Emp e3 = new Emp("tim", 22,"1","tim@uber.com");
        List<Emp> eids = Arrays.asList(e1, e2, e3);
        int res1 = dynamicSqlMapper.addMultiEmpByList(eids);
        System.out.println(res1);
    }

}
