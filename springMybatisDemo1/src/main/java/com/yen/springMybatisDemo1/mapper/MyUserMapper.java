package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=xVdgzSkU6po&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=10

import com.yen.springMybatisDemo1.bean.MyUser;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserMapper {

    /**
     *     MyBatis facing interface programming :
     *
     *     1) namespace should use mapper class (com.yen.springMybatisDemo1.mapper.MyUserMapper in MyUserMapper.xml)
     *     2) SQL method name (in MyUserMapper.java) is as same as SQL id in xml (in MyUserMapper.xml)
     */

    int insertUser(MyUser user);

}
