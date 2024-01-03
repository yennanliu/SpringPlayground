package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.lock.DistributedRedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis fair Lock with client library (Redisson)
 *
 *   - https://youtu.be/_7VL1DUlq1Q?si=wgc5NhvTT6lFgj9Q&t=271
 *
 */
@Service
public class StockServiceRedissonFairLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     *
     *  Fair lock testing
     *
     *  - https://youtu.be/_7VL1DUlq1Q?si=p15d3zQIkbuZbZkM&t=350
     *
     */
    public void testFairLock(Long id) {

        System.out.println(" testFairLock start =============== " + id);

        RLock fairLock = redissonClient.getFairLock("fairLock");

        // lock
        fairLock.lock();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            // unlock
            fairLock.unlock();
        }
    }

}
