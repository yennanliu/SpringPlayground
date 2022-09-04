package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=dNLGsANJ790&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=VqjaBphBdH4&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=50
// https://www.youtube.com/watch?v=t0pYgJu_nJ0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=52
// https://www.youtube.com/watch?v=bUXDOzn1phg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=52
// https://www.youtube.com/watch?v=Be9IYx1718k&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=54
// https://www.youtube.com/watch?v=ht97kZOvYCI&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=55

import com.yen.springMybatisDemo1.bean.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DynamicSqlMapper {

    /** multi conditions select */
    List<Emp> getEmpByCondition(Emp emp);

    /** multi conditions select V2 */
    List<Emp> getEmpByCondition2(Emp emp);

    /** multi conditions select V3 */
    List<Emp> getEmpByCondition3(Emp emp);

    /** test : choose, when, otherwise */
    List<Emp> getEmpByChoose(Emp emp);

    /** for-each : batch delete V1 */
    int deleteMultiEmpByArray(@Param("eids") Integer[] eids);

    /** for-each : batch delete V2 */
    int deleteMultiEmpByArray2(@Param("eids") Integer[] eids);

    /** for-each : batch delete V3 */
    int deleteMultiEmpByArray3(@Param("eids") Integer[] eids);

    /** for-each add insert (list) */
    int addMultiEmpByList(@Param("emps") List<Emp> emps);

}
