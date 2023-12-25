package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.mapper.StockMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
 *
 *   - Approaches
 *      - https://youtu.be/QxIAt_xgfA0?si=H4lEG_11e5n8ld7Y&t=108
 *      - https://youtu.be/GEsGglOPWw8?si=0Np9hZZRWA49Eodj&t=37
 *
 *      1) local JVM (not recommended) (only work for single instance, singleton)
 *      2) redis optimistic lock : watch multi exec
 *          -> In this demo (StockServiceRedis)
 *      3) redis distribution
 */

@Service
public class StockServiceRedis { // default : Singleton (@Scope("Singleton"))

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

        // https://youtu.be/GEsGglOPWw8?si=7PAlkNJ2ARCJWhvj&t=297
//        stringRedisTemplate.execute(new SessionCallback<Object>() {
//            @Override
//            public Object execute(RedisOperations operations) throws DataAccessException {
//                return null;
//            }
//        });

        /**
         *  redis watch
         *
         *   https://youtu.be/GEsGglOPWw8?si=0Np9hZZRWA49Eodj&t=37
         *
         * -- 1) watch : monitor 1 or multiple key value,
         * --         if key val changed before transaction execution (exec),
         * --         then cancel transaction exec
         */
        stringRedisTemplate.watch("stock");

        // 1) get stock amount
        String stock = stringRedisTemplate.opsForValue().get("stock");

        // 2) check if stock is enough
        if (stock != null && stock.length() != 0){
            Integer amount = Integer.valueOf(stock);

            /** redis multi
             *
             *  open a transaction
             */
            stringRedisTemplate.multi(); // io.lettuce.core.RedisCommandExecutionException: ERR EXEC without MULTI

            // 3) update stock to DB
            if (amount > 0){
                stringRedisTemplate.opsForValue().set("stock", String.valueOf(amount-1));
                /**  redis exec
                 *
                 *   execution a transaction
                 */
                stringRedisTemplate.exec();
            }
        }
    }

}
