package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=ozomvpRfzSU&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=57
// https://www.youtube.com/watch?v=kvf8-YwsuAc&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=57

import com.yen.springMybatisDemo1.bean.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CacheMapper {

    Emp getEmpByEid(@Param("eid") Integer eid);
    void insertEmp(Emp emp);
}
