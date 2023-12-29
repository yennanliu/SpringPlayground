package com.yen.SpringDistributionLock.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 *  Factory for constructing DistributedLock (Mysql, Redis, Zookeeper...)
 *
 *   - Factory design pattern
 *
 *   - DistributedLockFactory is registered to service class, then get different DistributedLock per case
 *      - avoid hardcode
 *
 *   - https://youtu.be/V5iKz8HPiI4?si=nBdyK2oOizuIxAlI&t=503
 */
@Component
public class DistributedLockFactory {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public DistributedRedisLock getRedisLock(String lockName){

        return new DistributedRedisLock(lockName, stringRedisTemplate);
    }

}
