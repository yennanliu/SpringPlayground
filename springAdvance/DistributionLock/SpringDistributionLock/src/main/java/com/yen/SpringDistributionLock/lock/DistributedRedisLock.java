package com.yen.SpringDistributionLock.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *   Distributed lock implemented with Redis
 *
 *   1. implement JUC lock interface
 *
 *   2. only implement Lock, Unlock methods
 *
 *   3.
 *      lock() uses this.tryLock();
 *      tryLock() uses this.tryLock(-1L, TimeUnit.SECONDS);
 *      so, the actual implementation logic resides in tryLock(long time, TimeUnit unit) method
 *
 *   https://youtu.be/V5iKz8HPiI4?si=qLzp6hSzaWD3tVLf&t=81
 */
// implement JUC lock interface
// NO need to register now, since now we have DistributedLockFactory calls DistributedRedisLock  :  https://youtu.be/V5iKz8HPiI4?si=w_mkUNpXisMzTLuP&t=539
//@Component // make sure this class can be registered into spring container, so below @Autowired StringRedisTemplate works
public class DistributedRedisLock implements Lock {

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    /** NOTE !!!
     *
     *
     *   since now DistributedRedisLock is NOT registered to spring container (register DistributedLockFactory instead)
     *   -> so @Autowired StringRedisTemplate is NOT working
     *   -> so we need to add StringRedisTemplate as DistributedRedisLock constructor attr
     *
     *   - https://youtu.be/V5iKz8HPiI4?si=Dq8F8DXjPwbGiuqA&t=789
     *
     */

    private String uuid;

    private long expire = 30;

    // attr
    private String lockName;
    private StringRedisTemplate stringRedisTemplate;

    // constructor
    public DistributedRedisLock(String lockName, StringRedisTemplate stringRedisTemplate, String uuid){

        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
        this.uuid =  uuid; //UUID.randomUUID().toString(); // "66-666-666";
    }

    @Override
    public void lock() {

        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        try {
            return this.tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }
        return false;
    }

    /** ACTUAL IMPLEMENTATION METHOD */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        if (time == -1){
            this.expire = unit.toSeconds(time); // transform to second
        }

        // https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_reentrantLock_lua.sql
        String luaScript = "if redis.call('exists', KEYS[1]) == 0 " +
                "or redis.call('hexists', KEYS[1], ARGV[1]) == 1 " +
                "then " +
                "redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "redis.call('expire', KEYS[1], ARGV[2]) " +
                "return 1 " +
                "else return 0 " +
                "end";

//        stringRedisTemplate.execute(
//                new DefaultRedisScript<>(luaScript, Boolean.class),
//                Arrays.asList(lockName),
//                this.uuid,
//                String.valueOf(this.expire)
//        );

        // if false, means can't get lock successfully, sleep
        System.out.println(">>> (tryLock) lockName = " + lockName + " uuid = " + this.uuid + " expire time = " + this.expire + " ID = " + getId()) ;
        Boolean res = stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), getId(), String.valueOf(this.expire));

        System.out.println("--------> (tryLock) res = " + res);

        while (!stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), getId(), String.valueOf(this.expire))){
            System.out.println("can't get lock, sleep 50 milliseconds");
            Thread.sleep(50);
            // retry
            //this.tryLock();
        }

        return true;
    }


    /** Unlock method */
    @Override
    public void unlock() {

        // https://github.com/yennanliu/SpringPlayground/blob/main/springAdvance/DistributionLock/SpringDistributionLock/sql/redis_reentrantLock_lua.sql
        String luaScript = "if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 0 " +
                "then return nil " +
                "elseif redis.call('HINCRBY', KEYS[1], ARGV[1], -1) == 0 " +
                "then return redis.call('del', KEYS[1]) " +
                "else return 0 " +
                "end";

        // NOTE !!! DO NOT use Boolean as return type, since nul, 0 are both recognized as false in java
        //          -> use Long type instead
        // https://youtu.be/V5iKz8HPiI4?si=VW4P0Zkp0JvAN_4o&t=1305
        System.out.println(">>> (unlock) lockName = " + lockName + " uuid = " + uuid + " Id = " + getId());
        Long flag = this.stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), Arrays.asList(lockName), getId());

        if (flag == null){
            throw new IllegalMonitorStateException("Unlock failed : this lock is NOT belong to current thread");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    // local method

    /**
     *  create unique ID for thread
     *
     *  logic : uuid + "-" + threadId
     *
     *  - https://youtu.be/vGSAzGKI2H4?si=oT-jcFFmFfMIY8tx&t=610
     */
    private String getId(){

        return uuid + "-" + Thread.currentThread().getId();
    }

}
