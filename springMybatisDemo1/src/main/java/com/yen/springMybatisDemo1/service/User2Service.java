package com.yen.springMybatisDemo1.service;

// https://blog.csdn.net/feinifi/article/details/88769101

import com.yen.springMybatisDemo1.bean.Pager;
import com.yen.springMybatisDemo1.bean.User;
import com.yen.springMybatisDemo1.mapper.User2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class User2Service {

    @Autowired
    User2Mapper user2Mapper;

    public Pager<User> findByPager(int page, int size){

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("page", (page-1) * size);
        params.put("size", size);

        Pager<User> pager = new Pager<User>();
        List<User> list = user2Mapper.findByPager(params);

        pager.setRows(list);
        pager.setTotal(user2Mapper.count());

        System.out.println(">>> pager = " + pager);

        return pager;
    }

    public long getTotal(){
        return user2Mapper.count();
    }

}
