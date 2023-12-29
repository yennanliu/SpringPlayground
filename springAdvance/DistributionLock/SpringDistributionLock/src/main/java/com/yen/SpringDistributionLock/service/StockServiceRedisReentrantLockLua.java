package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.lock.DistributedRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis Distribution ReentrantLock lock for Stock service
 *
 *
 * https://youtu.be/V5iKz8HPiI4?si=FydqKDhNXYO779hr&t=18
 */

@Service
public class StockServiceRedisReentrantLockLua {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DistributedRedisLock distributedRedisLock;

    public void deduct() {

        String stock = stringRedisTemplate.opsForValue().get("stock").toString();

        // 2) check if stock is enough
        if (stock != null && stock.length() != 0) {

            Integer amount = Integer.valueOf(stock);

            // 3) update stock to DB
            if (amount > 0) {
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(amount - 1));
            }
        }
    }


}
