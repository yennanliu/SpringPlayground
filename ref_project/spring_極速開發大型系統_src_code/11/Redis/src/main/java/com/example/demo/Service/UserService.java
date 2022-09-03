package com.example.demo.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author longzhonghua
 * @data 2/21/2019 6:25 PM
 * 快取就在這層工作
 * //@Cacheable將查詢結果快取到redis中，（key="#p0"）指定傳入的第一個參數作為redis的key。
 * //
 * //@CachePut，指定key，將更新的結果同步到redis中
 * //
 * //@CacheEvict，指定key，移除快取資料，allEntries=true,方法呼叫後將立即清除快取
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Cacheable(key ="#p0")
    public User selectUser(String id){
        System.out.println("select");
        return userMapper.findById(id);
    }

    @CachePut(key = "#p0")
    public void updataById(User user){
        System.out.println("update");
        userMapper.updateById(user);
    }

    //若果指定為 true，則方法呼叫後將立即清理所有快取
    @CacheEvict(key ="#p0",allEntries=true)
    public void deleteById(String id){
        System.out.println("delete");
        userMapper.deleteById(id);
    }
}