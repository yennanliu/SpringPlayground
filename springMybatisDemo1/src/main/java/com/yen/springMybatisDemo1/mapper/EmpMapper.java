package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=D_DDE_o7ks0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=gk_pm_Uaa_Y&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=42
// https://www.youtube.com/watch?v=3DntOk8Nj0A&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=43
// https://www.youtube.com/watch?v=bnTL0IZ6QXk&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=44
// https://www.youtube.com/watch?v=I4obnFs_CB8&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=47

import com.yen.springMybatisDemo1.bean.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpMapper {

    /** get all employees */
    List<Emp> getAllEmp();

    /** get all employees V2 */
    List<Emp> getAllEmp2();

    /** get employee and his/her department (multi-one mapping) */
    Emp getEmpAndDept(@Param("eid") Integer eid);

    /** via 分步查詢 (steps SQl), get employee and his/her department (multi-one mapping)
     *
     *   step 1) : get employee info
     */
    Emp getEmpAndDeptByStep1(@Param("eid") Integer eid);

    /**
     * dept and all employees in dept  (via step SQL 分步查詢)
     *
     *   step 2) get emp info with did
     */
    List<Emp> getDeptAndEmpByStep2(@Param("did") Integer did);
}
