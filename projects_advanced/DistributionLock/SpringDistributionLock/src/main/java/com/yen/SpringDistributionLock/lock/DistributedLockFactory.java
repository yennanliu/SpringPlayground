package com.yen.SpringDistributionLock.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *  Factory for constructing DistributedLock (Mysql, Redis, Zookeeper...)
 *
 *   - Factory design pattern
 *
 *   - DistributedLockFactory is registered to service class,
 *     then get different DistributedLock per case
 *
 *   - avoid hardcode
 *
 *   - https://youtu.be/V5iKz8HPiI4?si=nBdyK2oOizuIxAlI&t=503
 *
 *   NOTE !!!
 *
 *   - DistributedLockFactory is a singleton (單例對象),
 *     -> only init once (only 1 instance) in spring boot container
 *     -> can use it to create a single uuid used by all threads !!!
 *
 *   - https://youtu.be/vGSAzGKI2H4?si=_3wRaffdTzypmQ6c&t=468
 */
@Component
public class DistributedLockFactory {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private String uuid;

    /** NOTE !!!
     *
     *  since Spring boot create an instance (no args) with @Component,
     *  we can init uuid in no args constructor
     *
     *  https://youtu.be/vGSAzGKI2H4?si=v4LeAii0IP7DVj4s&t=495
     */
    public DistributedLockFactory(){

        this.uuid = UUID.randomUUID().toString();
    }

    public DistributedRedisLock getRedisLock(String lockName){

        return new DistributedRedisLock(lockName, stringRedisTemplate, uuid);
    }

}
