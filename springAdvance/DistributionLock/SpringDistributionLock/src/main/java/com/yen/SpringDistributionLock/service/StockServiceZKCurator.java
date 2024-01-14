package com.yen.SpringDistributionLock.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 *  StockService with ZK Curator lock
 *
 *      - https://youtu.be/WRo8Fbfb0Ys?si=gHb_vMoDyvzMDIAS&t=159
 */
@Service
public class StockServiceZKCurator {


    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void deduct() {

        // init InterProcessMutex
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator/locks");

        try {

            // get lock
            mutex.acquire();

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
            throw new RuntimeException(e);
        } finally {
            // unlock
            try {
                mutex.release();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
