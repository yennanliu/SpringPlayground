package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.lock.DistributedRedisLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.redisson.api.RSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * StockService with ZK Curator lock
 * <p>
 * - https://youtu.be/WRo8Fbfb0Ys?si=gHb_vMoDyvzMDIAS&t=159
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

            /**
             *  Test Curator Lock reentrantLock feature
             *
             *   - Note : in order to make sure threads are using same lock,
             *            below we pass mutex to testCuratorReentrantLock method
             *            -> less flexibility (cons when use Curator)
             */
            this.testCuratorReentrantLock(mutex);

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

    /**
     * Test Curator Lock reentrantLock feature
     * <p>
     * https://youtu.be/Zx-r9bKJ_Dk?si=KCr20aSLIRsa1CaM&t=15
     */
    public void testCuratorReentrantLock(InterProcessMutex mutex) throws Exception {

        //InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator/locks");
        // lock
        mutex.acquire();
        System.out.println("test testCuratorReentrantLock");
        // unlock
        mutex.release();
    }


    /**
     * service method for ZK Curator - InterProcessReadWriteMutex : 可重入讀寫鎖
     * <p>
     * https://youtu.be/LCiEhaqyJ38?si=Dwih5Jqyd5DaEpiD&t=136
     */
    public void testZKCuratorReadLock() {

        String zkPath = "/curator/rwlock";

        try {
            // init lock object
            InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(curatorFramework, zkPath);
            // get read lock
            readWriteLock.readLock().acquire(10, TimeUnit.SECONDS); // auto release lock after 10 sec
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // release lock
            //readWriteLock.readLock().release();
        }
    }

    public void testZKCuratorWriteLock() {

        String zkPath = "/curator/rwlock";

        try {
            // init lock object
            InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(curatorFramework, zkPath);
            // get write lock
            readWriteLock.writeLock().acquire(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** Curator InterProcessSemaphoreV2 demo
     *
     *  https://youtu.be/JgIsCOyzYJQ?si=fSH7O0LpujDM1Jyu&t=68
     */
    public void testSemaphore() {

        String zkPath = "/curator/locks";

        // init curator InterProcessSemaphoreV2, 5 : resource amount
        InterProcessSemaphoreV2 interProcessSemaphoreV2 = new InterProcessSemaphoreV2(curatorFramework, zkPath, 5);

        try{
            //Semaphore semaphore = new Semaphore(3);

            // get resource
            Lease lease = interProcessSemaphoreV2.acquire();
            System.out.println("#### (zk curator)" + Thread.currentThread().getName() + " get resource !");
            TimeUnit.SECONDS.sleep(new Random().nextInt(10) + 10);
            System.out.println("--------> (zk curator) " + Thread.currentThread().getName() + " release resource ==============");
            // save log to redis
            // <--- push from right : so we can view most early log from begin
            stringRedisTemplate.opsForList().rightPush("log (zk curator) ", "#### " + Thread.currentThread().getName() + " get resource !");
            stringRedisTemplate.opsForList().rightPush("log (zk curator) ", "--------> " + Thread.currentThread().getName() + " release resource ==============");

            // Need to manually release resource, so other thread can get resource
            interProcessSemaphoreV2.returnLease(lease);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // https://youtu.be/GKdBxd0e7sI?si=xVIEWT32J0BmE46m&t=127
    public void testZKShareCount() {

        try{
            String zkPath = "/curator/share_count";
            SharedCount shareCount = new SharedCount(curatorFramework, zkPath, 100);
            // need to start first
            shareCount.start();
            int count = shareCount.getCount(); // get current count
            System.out.println(">>> (before) shared count = " + count);
            int random = new Random().nextInt(10);
            shareCount.setCount(random);
            System.out.println(">>> (after) shared count = " + random);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
