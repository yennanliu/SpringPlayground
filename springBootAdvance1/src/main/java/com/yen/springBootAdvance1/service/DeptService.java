package com.yen.springBootAdvance1.service;

// https://www.youtube.com/watch?v=FhlRZRshF14&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=13

import com.yen.springBootAdvance1.bean.Department;
import com.yen.springBootAdvance1.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DeptService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Cacheable(cacheNames = "dept", key = "id")
    public Department getDeptById(Integer id){
        System.out.println(">>> query Department ..." + id);
        Department department = departmentMapper.getDeptById(id);
        return department;
    }

}
