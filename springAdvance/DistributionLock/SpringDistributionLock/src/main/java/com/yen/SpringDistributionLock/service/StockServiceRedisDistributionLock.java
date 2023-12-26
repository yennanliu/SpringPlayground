package com.yen.SpringDistributionLock.service;

// https://youtu.be/kbO_HpxEcQ4?si=IPjLadwtplCmX-Pe&t=151

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Redis Distribution lock for Stock service
 *
 *
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
        //Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "111");

        /**
         *  Note !!!
         *
         *   1) Retry : recursion call
         *
         *   https://youtu.be/kUk5FK2o780?si=ajnmzgcNASP038UM&t=634
         *
         *   if recursion call, use below structure:
         *
         *   if (!lock){
         *      // sleep, recursion call
         *   }else{
         *      // deduct logic
         *      // unlock logic
         *   }
         *
         *  -> request can ONLY do deduct when it gets lock at first time
         *  -> (request is NOT allowed to deduct via retry (if it CAN'T get lock at first time ))
         *
         *
         *
         *  2) Retry : Iteration call
         *
         *   https://youtu.be/HI9lQmCTPPc?si=H3rcL8UUUS2fa14w&t=78
         *
         *  if iteration call, use below structure:
         *
         *      while(! can_get_lock()){
         *          //sleep
         *      }try{
         *          // deduct logic
         *      }finally{
         *          // unlock logic
         *      }
         */
        // if get lock fail,
        // method 1) retry (recursion call), cons : recursion may cause stackoverflow
        // method 2) retry via iteration : https://youtu.be/HI9lQmCTPPc?si=eLxE7OgPwkiMOecF&t=29
        while (!stringRedisTemplate.opsForValue().setIfAbsent("lock", "111")) {

            try {
                Thread.sleep(50); // sleep 50 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("get lock fail, sleep and retry ...");

        }
        try {

            /** Set lock expire time
             *
             *   below code NOT works (if server down before setting expire time, still no expire time, not ATOM)
             *
             *  https://youtu.be/h_thAi4SCEQ?si=6xOS8cfpoMdwCJ9L&t=363
             */
            //stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);

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

