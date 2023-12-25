package com.yen.SpringDistributionLock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *   Redis Optimistic lock for Stock service (redis optimistic lock : watch multi exec)
 *
 *   - https://youtu.be/RtrU2dGUxIQ?si=av_CvxqEnkex9NAt
 *   - https://youtu.be/GEsGglOPWw8?si=1bTJopcdH9gzpU7d
 *   - /sql/redis_optimistic_lock_demo.sql
 *
 *   #### Steps
 *      - watch -> multi -> exec
 *
 *   - Insert data to redis with below cmd:
 *      set stock 5000
 *
 *   - Approaches
 *      - https://youtu.be/QxIAt_xgfA0?si=H4lEG_11e5n8ld7Y&t=108
 *      - https://youtu.be/GEsGglOPWw8?si=0Np9hZZRWA49Eodj&t=37
 *
 *      1) local JVM (not recommended) (only work for single instance, singleton)
 *
 *      *** 2) redis optimistic lock : watch multi exec
 *          -> In this demo (StockServiceRedisOptimisticLock)
 *
 *          - pros
 *          - cons
 *              - low performance
 *
 *      3) redis distribution lock
 */

@Service
public class StockServiceRedisOptimisticLock { // default : Singleton (@Scope("Singleton"))

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
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                //return null;
                /**
                 *  redis watch (Optimistic lock)
                 *
                 *   https://youtu.be/GEsGglOPWw8?si=0Np9hZZRWA49Eodj&t=37
                 *
                 * -- 1) watch : monitor 1 or multiple key value,
                 * --         if key val changed before transaction execution (exec),
                 * --         then cancel transaction exec
                 */
                operations.watch("stock");  // RedisOperations also has StringRedisTemplate implementation, so we can use it (RedisOperations operations) in this anomalous method

                // 1) get stock amount
                String stock = operations.opsForValue().get("stock").toString();

                // 2) check if stock is enough
                if (stock != null && stock.length() != 0){
                    Integer amount = Integer.valueOf(stock);

                    /** redis multi (Optimistic lock)
                     *
                     *  open a transaction
                     */
                    operations.multi(); // use stringRedisTemplate.execute to avoid this error : io.lettuce.core.RedisCommandExecutionException: ERR EXEC without MULTI

                    // 3) update stock to DB
                    if (amount > 0){

                        operations.opsForValue().set("stock", String.valueOf(amount-1));

                        /**  redis exec (Optimistic lock)
                         *
                         *   execution a transaction
                         */
                        List res = operations.exec();
                        // if result is null, then retry (recursion call)
                        if (res == null || res.size() == 0){
                            try {
                                Thread.sleep(40);
                                deduct();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                        return res;
                    }
                }

                return null;
            }
        });

    }

}
