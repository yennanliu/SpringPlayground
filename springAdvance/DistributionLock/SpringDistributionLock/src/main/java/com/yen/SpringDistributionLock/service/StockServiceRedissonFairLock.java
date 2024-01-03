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

//    public void deduct() {
//
//        // get lock
//        RLock lock = redissonClient.getLock("lock");
//
//        // lock
//        lock.lock();
//
//        try {
//
//            // 1) get stock amount
//            String stock = stringRedisTemplate.opsForValue().get("stock").toString();
//
//            // 2) check if stock is enough
//            if (stock != null && stock.length() != 0) {
//
//                Integer amount = Integer.valueOf(stock);
//
//                // 3) update stock to DB
//                if (amount > 0) {
//
//                    stringRedisTemplate.opsForValue().set("stock", String.valueOf(amount - 1));
//                }
//            }
//
//            this.testThreadTestReentrantLock();
//
//        } finally {
//            // unlock
//            lock.unlock();
//        }
//    }
//
//
//    public void testThreadTestReentrantLock(){
//
//        RLock lock = this.redissonClient.getLock("lock");
//        // lock
//        lock.lock();
//        System.out.println("(Redisson) test ThreadTestReentrantLock");
//        // unlock
//        lock.unlock();
//    }

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
