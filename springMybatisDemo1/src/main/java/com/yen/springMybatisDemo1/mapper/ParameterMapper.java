package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=_5a7CjR-XSw&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=22

import com.yen.springMybatisDemo1.bean.MyUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParameterMapper {

    /** select all users */
    List<MyUser> getAllUser();
}
