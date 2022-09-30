package com.yen.springCourseSystem.mapper;

// book p. 253

import com.yen.springCourseSystem.bean.User;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {

    User loadUserByUserName(String userName);
}
