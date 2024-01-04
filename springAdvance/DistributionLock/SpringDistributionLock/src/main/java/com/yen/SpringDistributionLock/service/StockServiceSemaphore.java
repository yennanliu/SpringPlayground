package com.yen.SpringDistributionLock.service;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *  Distribution Semaphore service with redis
 *
 *
 *  https://youtu.be/LHd8_ATmBD8?si=VZmAjsAIBt2ZSm6b&t=602
 *
 *  - JUC Semaphore, can only work in single node (NOT working in cluster deployment)
 *  - so we need Redisson Semaphore
 *
 */

@Service
public class StockServiceSemaphore {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // JUC Semaphore, can only work in single node (NOT working in cluster deployment)
    //Semaphore semaphore = new Semaphore(3);

    public void testSemaphore() {

        // https://youtu.be/LHd8_ATmBD8?si=4l5QZtxXUiLgSb2F&t=941
        // setup semaphore via Redisson
        // it's OK to put below in or outside method
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        // https://youtu.be/LHd8_ATmBD8?si=wDFRVg5_SZNkEpyd&t=1083
        // setup resource amount
        semaphore.trySetPermits(3);

        try{

            //Semaphore semaphore = new Semaphore(3);

            // get resource
            semaphore.acquire();
            System.out.println("#### " + Thread.currentThread().getName() + " get resource !");
            TimeUnit.SECONDS.sleep(new Random().nextInt(10) + 10);
            System.out.println("--------> " + Thread.currentThread().getName() + " release resource ==============");
            // save log to redis
            // <--- push from right : so we can view most early log from begin
            stringRedisTemplate.opsForList().rightPush("log", "#### " + Thread.currentThread().getName() + " get resource !");
            stringRedisTemplate.opsForList().rightPush("log", "--------> " + Thread.currentThread().getName() + " release resource ==============");

            // release resource
            semaphore.release();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
