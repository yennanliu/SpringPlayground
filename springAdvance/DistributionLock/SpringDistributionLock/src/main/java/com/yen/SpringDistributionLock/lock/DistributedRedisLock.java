package com.yen.SpringDistributionLock.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *   Distributed lock implemented with Redis
 *
 *   1. implement JUC lock interface
 *
 *   2. only implement Lock, Unlock methods
 *
 *   3.
 *      lock() uses this.tryLock();
 *      tryLock() uses this.tryLock(-1L, TimeUnit.SECONDS);
 *      so, the actual implementation logic resides in tryLock(long time, TimeUnit unit) method
 *
 *   https://youtu.be/V5iKz8HPiI4?si=qLzp6hSzaWD3tVLf&t=81
 */
// implement JUC lock interface
@Component // make sure this class can be registered into spring container, so below @Autowired StringRedisTemplate works
public class DistributedRedisLock implements Lock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void lock() {

        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        try {
            return this.tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return false;
    }

    /** ACTUAL IMPLEMENTATION METHOD */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        // https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_reentrantLock_lua.sql
        String luaScript = "if redis.call('exists', KEYS[1]) == 0 " +
                "or redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "redis.call('expire', KEYS[1], ARGV[2]) " +
                "return 1 " +
                "else return 0 " +
                "end";

        //stringRedisTemplate.opsForValue(luaScript);
        return false;
    }


    /** Unlock method */
    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
