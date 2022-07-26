package com.yen.springMybatisDemo1.mapper;

// https://dotblogs.com.tw/zjh/2018/09/28/mybatis_1

import com.yen.springMybatisDemo1.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public User select1(int id);
    public int insert1(User user);
    public int delete1(int id);
    public int update1(User user);
}
