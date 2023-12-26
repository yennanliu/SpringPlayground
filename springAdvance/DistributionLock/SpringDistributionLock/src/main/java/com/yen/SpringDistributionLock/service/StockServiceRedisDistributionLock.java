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

        /** add lock
         *
         *  lock : setnx
         *
         *  return type : Boolean (true : get lock success, false : get lock fail)
         */
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111");

        /**
         *  Note !!!
         *
         *   we need below logic :
         *
         *   if (!lock){
         *      ...
         *   }else{
         *       ...
         *   }
         *
         *  -> request can ONLY do deduct when it gets lock at first time
         *  -> (request is NOT allowed to deduct via retry (if it CAN'T get lock at first time ))
         *
         *  https://youtu.be/kUk5FK2o780?si=ajnmzgcNASP038UM&t=634
         *
         *
         */
        // if get lock fail, retry (recursion call)
        if (!lock) {
            try {
                Thread.sleep(50); // sleep 50 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("get lock fail, sleep and retry ...");
            this.deduct();
        } else {
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

                /** unlock
                 *
                 *  unlock : del
                 */
                stringRedisTemplate.delete("lock");
            }
        }

    }

}

