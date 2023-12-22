package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.mapper.StockMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 *   Stock service deal data consistency with Redis
 *
 *   - https://youtu.be/RtrU2dGUxIQ?si=av_CvxqEnkex9NAt
 *
 *   - Insert data to redis with below cmd:
 *      set stock 5000
 */

@Service
public class StockServiceRedis { // default : Singleton (@Scope("Singleton"))

    @Autowired
    StockMapper stockMapper;

    //private ReentrantLock lock = new ReentrantLock();

    /**
     *  StringRedisTemplate VS RedisTemplate
     *
     *   - https://youtu.be/RtrU2dGUxIQ?si=F_BGheMKAofdsjwL&t=262
     *
     *   - RedisTemplate uses JDK serialization, so data is transformed to binary form then write to redis
     *     -> hard to debug
     *     -> can use String or Json serialization
     *
     *   - StringRedisTemplate already implemented String serialization
     */

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //private RedisTemplate redisTemplate;

    public void deduct() {

        // 1) get stock amount
        String stock = stringRedisTemplate.opsForValue().get("stock");

        // 2) check if stock is enough
        if (stock != null && stock.length() != 0){
            Integer amount = Integer.valueOf(stock);
            // 3) update stock to DB
            if (amount > 0){
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(amount-1));
            }
        }
    }

}
