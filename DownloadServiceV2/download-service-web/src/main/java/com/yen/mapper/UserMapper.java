package com.yen.mapper;

import com.yen.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public User getUserByName(String name);

    @Insert("INSERT INTO user (`name`, `age`) VALUES(#{name},#{age}")
    public void insertUser(User user);
}
