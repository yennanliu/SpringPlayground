package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.zookeeper.ZKClient;
import com.yen.SpringDistributionLock.zookeeper.ZKDistributionLock;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockServiceZK {

    @Autowired
    private ZKClient zkClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // constructor
//    StockServiceZK(){
//        this.zkClient = new ZKClient();
//    }

    public void deduct() {

        // get lock
        ZKDistributionLock lock = zkClient.getLock("lock");

        try {
            // lock
            lock.lock();

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
            // unlock
            lock.unlock();
        }
    }

}
