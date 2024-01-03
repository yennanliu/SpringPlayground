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
 * Redis Lock with client library (Redisson)
 * <p>
 * - https://youtu.be/ynJQouCae4I?si=yAxtOEJ1ZCPHGLpk&t=570
 * <p>
 * - https://youtu.be/Sld_bKriREo?si=LV3gX_JzsFIUyw7G&t=556
 */
@Service
public class StockServiceRedisson {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    public void deduct() {

        // get lock
        RLock lock = redissonClient.getLock("lock");

        // lock
        lock.lock();
        // https://youtu.be/Sld_bKriREo?si=syTRREvTmv8Y5VpN&t=620
        // auto release lock after 10 sec,  so no need to relase lock
        //lock.lock(10, TimeUnit.SECONDS);

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

            this.testThreadTestReentrantLock();

//            // https://youtu.be/su4F77h0mSE?si=hHThlUfEa_oJSRRx&t=399
//            TimeUnit.SECONDS.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } finally {
            // unlock
            lock.unlock();
        }
    }


    public void testThreadTestReentrantLock(){

        RLock lock = this.redissonClient.getLock("lock");
        // lock
        lock.lock();
        System.out.println("(Redisson) test ThreadTestReentrantLock");
        // unlock
        lock.unlock();
    }

}
