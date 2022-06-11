package com.yen.springBootAdvance1.service;

// https://www.youtube.com/watch?v=FhlRZRshF14&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=13

import com.yen.springBootAdvance1.bean.Department;
import com.yen.springBootAdvance1.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *  if we save dept cache to redis with json form
 *      -> we can save it to cache (serialization), but may face error when read cache from redis (deserialization)
 *      -> solution : implement serialization/deserialization for dept as well (MyRedisConfig)
 *
 */
@Service
public class DeptService {

    @Autowired
    DepartmentMapper departmentMapper;

    /** V1 : implement cache via annotation */
    //@Cacheable(cacheNames = "dept", key= "#dept.id")
    @Cacheable(cacheNames = {"dept"})
    //@Cacheable(cacheNames = {"dept"}, cacheManager = "deptCacheManager")
    public Department getDeptById(Integer id){
        System.out.println(">>> query Department ..." + id);
        Department department = departmentMapper.getDeptById(id);
        return department;
    }

    /** V2 : implement cache via code
     *   - https://youtu.be/FhlRZRshF14?t=1461
     */

}
