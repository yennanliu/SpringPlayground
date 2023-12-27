package com.yen.SpringDistributionLock.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Implement Redis ATOM Distribution lock with Lua script
 *
 *
 *   - https://youtu.be/ZLNbNbfVXEw?si=EchNpe781yLP7mwu&t=651
 *   - https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/lua_redos_lock_atom.lua
 */


@Service
public class StockServiceRedisDistributionLockLua {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void deduct() {

        // use UUID as lock ID : https://youtu.be/uF3sVyB9Dx0?si=8FFJEytdcqEu8Xyz&t=524
        String uuid = UUID.randomUUID().toString();

        while (!stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS)) {

            try {
                Thread.sleep(50); // sleep 50 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("get lock fail, sleep and retry ...");

        }
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
             *
             *  implement with Lua script
             *
             *   - https://youtu.be/ZLNbNbfVXEw?si=ZTHC36KukFKoxiUD&t=651
             */
            // check if same lock, then lock (via uuid)
//            if (StringUtils.equals(stringRedisTemplate.opsForValue().get("lock"), uuid)){
//                stringRedisTemplate.delete("lock");
//            }else{
//                System.out.println("will NOT unlock, uuid are different");
//            }

            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else " +
                    "return 0 " +
                    "end";
            stringRedisTemplate.execute(
                    new DefaultRedisScript<>(luaScript), Arrays.asList("lock"), uuid);
        }
    }

}
