package com.yen.springBootAdvance1.service;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=4
// https://www.youtube.com/watch?v=4dRfvI1tnqs&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=5
// https://www.youtube.com/watch?v=LGleXiH-7QU&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=5

import com.yen.springBootAdvance1.bean.Employee;
import com.yen.springBootAdvance1.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     *  @Cacheable
     *
     *  -> cache method result, will use cache when there is same request again
     *
     *  -> CacheManager for cache management, every cache has its name in CacheManager
     *
     *  -> Cache param
     *      - cacheNames/value : cache name
     *      - key : key for cache (k-v), use method param name by default
     *          - use SpEl, example: #id (param id), #a0, #p0, #root.args[0]
     *      - keyGenerator : generate key, we can define our own keyGenerator as well
     *          - (can only use either key or keyGenerator at once)
     *      - CacheManager : declare which CacheManager to use
     *      - CacheResolver : similar as CacheManager
     *          - (can only use either CacheManager or CacheResolver at once)
     *      - condition : if condition is true, use cache
     *      - unless : if unless is true, NOT use cache
     *          - e.g. : unless = "#result == null"
     *      - async: whether use async in cache
     *
     *  - cache theory
     *
     *      1. AutoConfig class : CacheAutoConfiguration
     *
     *      2. cache conf class:
     *          - org.springFramework.boot.autoconfigure.cache.GenericCacheConfiguration
     *          - org.springFramework.boot.autoconfigure.cache.JCacheConfiguration
     *          - org.springFramework.boot.autoconfigure.cache.RedisCacheConfiguration ...
     *
     *      3. which caches are activated ?
     *          - org.springFramework.boot.autoconfigure.cache.SimpleCacheConfiguration
     *              -> register a cacheManager (ConcurrentMapCacheManager) to container
     *              -> receiver or create a ConcurrentMapCacheManager (ConcurrentMap type)
     *              -> save cache tp ConcurrentMap
     */
    @Cacheable(cacheNames = {"emp"})
    //@Cacheable(cacheNames = {"emp"}, condition = "#id>0")
    public Employee getEmp(Integer id){
        System.out.println(">>> query employee with id = " + id);
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }

}
