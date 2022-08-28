package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=bnTL0IZ6QXk&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=44
// https://www.youtube.com/watch?v=nvLKTBxgIdg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=46
// https://www.youtube.com/watch?v=I4obnFs_CB8&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=47

import com.yen.springMybatisDemo1.bean.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptMapper {

    /** via 分步查詢 (steps SQl), get employee and his/her department (multi-one mapping)
     *
     *   step 2) : get dept info via emp info
     */
    Dept getEmpAndDeptByStep2(@Param("did") Integer did);

    /** dept and all employees in dept */
    Dept getDeptAndEmp(@Param("did") Integer did);

    /**
     * dept and all employees in dept  (via step SQL 分步查詢)
     *
     *   step 1) get dept info
     */
    Dept getDeptAndEmpByStep1(@Param("did") Integer did);

}
