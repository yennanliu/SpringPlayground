package com.yen.springMybatisDemo1.mapper;

// https://blog.csdn.net/feinifi/article/details/88769101

import com.yen.springMybatisDemo1.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface User2Mapper {

    public List<User> findByPager(Map<String, Object> params);
    public long count();

}
