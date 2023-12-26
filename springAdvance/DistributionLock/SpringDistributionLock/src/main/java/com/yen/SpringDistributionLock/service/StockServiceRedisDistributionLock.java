package com.yen.SpringDistributionLock.service;

// https://youtu.be/kbO_HpxEcQ4?si=IPjLadwtplCmX-Pe&t=151

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * Redis Distribution lock for Stock service
 * <p>
 * https://youtu.be/kUk5FK2o780?si=WAeDZQHtFqPE3kdX&t=13
 */

@Service
public class StockServiceRedisDistributionLock {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void deduct() {

        // 1) get stock amount
        String stock = stringRedisTemplate.opsForValue().get("stock").toString();

        // 2) check if stock is enough
        if (stock != null && stock.length() != 0) {

            Integer amount = Integer.valueOf(stock);

            // 3) update stock to DB
            if (amount > 0) {

            }
        }

    }

}

