package com.yen.SpringDistributionLock.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *  ZK DistributionLock
 *
 *  - https://youtu.be/dcmhIij3eNM?si=botaWUkhGIXVimUX&t=34
 *  - https://youtu.be/dcmhIij3eNM?si=auTjc50ax8GpmIas&t=461
 *
 */
public class ZKDistributionLock implements Lock {

    // attr
    String lockName;
    ZooKeeper zooKeeper;

    private static final String ROOT_PATH = "/locks";

    // constructor
    public ZKDistributionLock(ZooKeeper zooKeeper, String lockName) {

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

    @Override
    public void lock() {

        // create ZK node, if can't create, keep retrying

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

        // delete ZK node

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    // method

}
