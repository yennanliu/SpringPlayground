package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=xVdgzSkU6po&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=10
// https://www.youtube.com/watch?v=UuYZjy-TXo0&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=13
// https://www.youtube.com/watch?v=Tj14NvhKaGk&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=14

import com.yen.springMybatisDemo1.bean.MyUser;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyUserMapper {

    /**
     *     MyBatis facing interface programming :
     *
     *     1) namespace should use mapper class (com.yen.springMybatisDemo1.mapper.MyUserMapper in MyUserMapper.xml)
     *     2) SQL method name (in MyUserMapper.java) is as same as SQL id in xml (in MyUserMapper.xml)
     *
     *     (one to one relation)
     *
     *     Note :
     *         -> mapping relation :
     *            table --> bean class (pojo) --> mapper interface -> mapper xml
     */

    /** add new user */
    int insertUser(MyUser user);

    /** modify user */
    void updateUser(int id, String username);

    /** delete user */
    void deleteUser(int id);

    /** select user by id */
    MyUser getUserById(int id);

    /** select all users */
    List<MyUser> getAllUser();

}
