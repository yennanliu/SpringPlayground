package com.yen.springMybatisDemo1.mapper;

// https://blog.csdn.net/feinifi/article/details/88769101

import com.yen.springMybatisDemo1.bean.User;

import java.util.List;
import java.util.Map;

public interface User2Mapper {

    public List<User> findByPager(Map<String, Object> params);
    public long count();

}
