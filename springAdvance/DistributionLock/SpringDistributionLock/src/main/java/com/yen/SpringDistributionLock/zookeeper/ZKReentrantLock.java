package com.yen.SpringDistributionLock.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *  ZK ReentrantLock
 *
 *   - https://youtu.be/0C-c0QEIP3A?si=O0ZlJ62GQAqxK2hY&t=254
 *
 */

public class ZKReentrantLock implements Lock {

    /**
     *  implement ZK ReentrantLock via ThreadLocal
     *
     *  https://youtu.be/0C-c0QEIP3A?si=O0ZlJ62GQAqxK2hY&t=254
     */
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();


    // attr
    String lockName;
    ZooKeeper zooKeeper;

    private static final String ROOT_PATH = "/locks";

    // constructor
    public ZKReentrantLock(ZooKeeper zooKeeper, String lockName) {

        // check if root path exists; if not, create
        try {
            if (zooKeeper.exists(ROOT_PATH, false) == null){
                zooKeeper.create(ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.zooKeeper = zooKeeper;
        this.lockName = lockName;
    }

    // method
    @Override
    public void lock() {

        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    /**
     *  create ZK node, if can't create, keep retrying
     *  https://youtu.be/rw6jIVHirvQ?si=fbGVH9knZ38uPFa4
     *
     */
    @Override
    public boolean tryLock() {

        try {

            /**
             *  Check first if ThreadLocal already has lock, if true, 重入 (+1)
             *
             *  https://youtu.be/0C-c0QEIP3A?si=r44n3eZGxLj6kunL&t=317
             *
             *
             *  flag != null : has Reentrant info (重入訊息)
             *  flag > 0 : has lock, but not yet release
             */
            Integer flag = THREAD_LOCAL.get();
            if (flag != null && flag > 0){
                THREAD_LOCAL.set(flag + 1);
                return true;
            }


            this.zooKeeper.create(ROOT_PATH + "/" + this.lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            /**
             *  record thread get Reentrant lock
             *
             *  - https://youtu.be/0C-c0QEIP3A?si=4qJonm7I8VCakRP7&t=414
             */
            THREAD_LOCAL.set(1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // retry
                System.out.println("Can't get lock, sleep 80 millisecond, and try ...");
                Thread.sleep(80);
                this.tryLock();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public void unlock() {

        try {
            // version = -1 : delete lock anyway
            this.zooKeeper.delete(ROOT_PATH + "/" + lockName, -1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
