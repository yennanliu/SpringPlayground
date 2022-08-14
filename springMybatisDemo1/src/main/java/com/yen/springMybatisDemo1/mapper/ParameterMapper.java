package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=_5a7CjR-XSw&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=22
// https://www.youtube.com/watch?v=kmPPvKs0G6Y&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=26
// https://www.youtube.com/watch?v=RZRKAEyAOfQ&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=27

import com.yen.springMybatisDemo1.bean.MyUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParameterMapper {

    /** select all users */
    List<MyUser> getAllUser();

    /** get user by username */
    MyUser getUserByName(String username);

    /** verify login */
    MyUser checkLogin(String username, String password);

    /** verify login V2 (param is a map) */
    MyUser checkLogin2(Map<String, Object> map);

}
