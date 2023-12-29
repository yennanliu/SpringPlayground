package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.lock.DistributedLockFactory;
import com.yen.SpringDistributionLock.lock.DistributedRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis Distribution ReentrantLock lock for Stock service
 * <p>
 * <p>
 * https://youtu.be/V5iKz8HPiI4?si=FydqKDhNXYO779hr&t=18
 */

@Service
public class StockServiceRedisReentrantLockLua {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

//    @Autowired
//    DistributedRedisLock distributedRedisLock;

    @Autowired
    DistributedLockFactory distributedLockFactory;

    public void deduct() {

        // get redis distribution lock
        DistributedRedisLock redisLock = this.distributedLockFactory.getRedisLock("lock");
        // lock
        redisLock.lock();

        try {
            // 1) get stock amount
            String stock = stringRedisTemplate.opsForValue().get("stock").toString();

            // 2) check if stock is enough
            if (stock != null && stock.length() != 0) {

                Integer amount = Integer.valueOf(stock);

                // 3) update stock to DB
                if (amount > 0) {
                    stringRedisTemplate.opsForValue().set("stock", String.valueOf(amount - 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisLock.unlock();
        }
    }

}
