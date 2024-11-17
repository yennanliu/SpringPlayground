package com.yen.SpringDistributionLock.service;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *  Redisson CountDownLatch demo
 *
 *  https://youtu.be/qsypEWwBLR8?si=9MgkPKREs0sLjy29&t=815
 */
@Service
public class StockServiceCountDownLatch {

    //CountDownLatch countDownLatch = new CountDownLatch(6);

    @Autowired
    private RedissonClient redissonClient;

    // init countdown val
    public void testLatch() {

        // init a countDownLatch
        RCountDownLatch cdl = redissonClient.getCountDownLatch("cdl");
        // set up resource
        cdl.trySetCount(6);

        try{
            // block thread
            cdl.await();
            // Lock room op
        }catch (Exception e){

        }

    }

    // countdown -= 1
    public void testCountDown() {

        // init a countDownLatch
        RCountDownLatch cdl = redissonClient.getCountDownLatch("cdl");
        // set up resource
        cdl.trySetCount(6);

        // leave room op
        cdl.countDown();
    }

}
