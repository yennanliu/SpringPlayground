package com.yen.SpringDistributionLock.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
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

        // https://youtu.be/XWq_GRvjJYI?si=_xxJe_uISXpVgU2d&t=714
        this.uuid = uuid + "-" + Thread.currentThread().getId(); //uuid; //UUID.randomUUID().toString(); // "66-666-666";
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
        System.out.println(">>> (tryLock) lockName = " + lockName + " uuid = " + this.uuid + " expire time = " + this.expire + " ID = " + this.uuid) ;
        Boolean res = stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), this.uuid, String.valueOf(this.expire));

        System.out.println("--------> (tryLock) res = " + res);

        while (!stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), this.uuid, String.valueOf(this.expire))){
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
        System.out.println(">>> (unlock) lockName = " + lockName + " uuid = " + uuid + " Id = " + this.uuid);
        Long flag = this.stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), Arrays.asList(lockName), this.uuid);

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
     *      - https://youtu.be/vGSAzGKI2H4?si=oT-jcFFmFfMIY8tx&t=610
     *
     *  update :
     *      - deal with parent, child thread
     *      - instead of having getId() method
     *      -  -> get uuid = uuid + "-" + Thread.currentThread().getId() in constructor directly
     *      - https://youtu.be/XWq_GRvjJYI?si=AA0ShbGHTE_B9QTI&t=697
     */
//    private String getId() {
//
//        return uuid + "-" + Thread.currentThread().getId();
//    }

    /**
     * renew lock expire time
     * - https://youtu.be/XWq_GRvjJYI?si=FM8_jL6qA3afTYzP&t=30
     */
    private void renewExpireTime() {

        String luaScript = "if redis.call('HEXISTS', KEYS[1], ARGV[1]) == 1 " +
                "then "
                + "return redis.call('EXPIRE', KEYS[1],  ARGV[2]) " +
                "else return 0 " +
                "end";

        // periodically run (定時任務) : run every one third period of expire time
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // if renew expire time success, delay this.expire * 1000 / 3, then renew again (recursive call)
                // if renew faled, means lock NOT existed anymore, so need to renew, no need to call method again
                //stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), getId(), String.valueOf(expire));
                if (stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(lockName), uuid, String.valueOf(expire))) {

                    renewExpireTime();
                }
            }
        }, this.expire * 1000 / 3);

    }


}
