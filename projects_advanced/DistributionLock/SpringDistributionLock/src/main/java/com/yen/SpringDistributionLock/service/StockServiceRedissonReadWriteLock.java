package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.lock.DistributedRedisLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis Read, Write Lock with client library (Redisson)
 *
 *   -  https://youtu.be/T___uj6PolA?si=m4b3iy5MbDsaQLEe&t=430
 *
 */
@Service
public class StockServiceRedissonReadWriteLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

//    /**
//     *
//     *  Fair lock testing
//     *
//     *  - https://youtu.be/_7VL1DUlq1Q?si=p15d3zQIkbuZbZkM&t=350
//     *
//     */
//    public void testFairLock(Long id) {
//
//        System.out.println(" testFairLock start =============== " + id);
//
//        RLock fairLock = redissonClient.getFairLock("fairLock");
//
//        // lock
//        fairLock.lock();
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }finally {
//            // unlock
//            fairLock.unlock();
//        }
//    }

    /**
     *
     *  Read lock testing
     *
     *  - https://youtu.be/T___uj6PolA?si=VztiJpUlMgfHwTU1&t=430
     *
     */
    public void testReadLock() {

        // get lock
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");

        // lock
        //rwLock.readLock().lock();
        rwLock.readLock().lock(10, TimeUnit.SECONDS);

        // read op

        // unlock
        //rwLock.readLock().unlock();
    }


    public void testWriteLock() {

        // get lock
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");

        // lock
        //rwLock.writeLock().lock();
        rwLock.writeLock().lock(10, TimeUnit.SECONDS);

        // write op

        // unlock
        //rwLock.writeLock().unlock();
    }

}
