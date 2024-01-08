package com.yen.SpringDistributionLock.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *  ZK BlockingLock
 *
 *  - 阻塞鎖
 *     - 利用 `ZK 臨時序列化節點`
 *
 *  - https://youtu.be/yAp8mbhxWlM?si=yfpVYi5e4N9DVNS9&t=414
 */
public class ZKBlockingLock implements Lock {

    // attr
    String lockName;
    ZooKeeper zooKeeper;

    private static final String ROOT_PATH = "/locks";

    // constructor
    public ZKBlockingLock(ZooKeeper zooKeeper, String lockName) {

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
             *  NOTE !!!
             *
             *   - Here we create blocking lock via ZK ephemeral serialization node (臨時序列化節點)
             *   - 阻塞鎖 利用 `ZK 臨時序列化節點`
             *
             *   - https://youtu.be/yAp8mbhxWlM?si=7B48O3JvfV43e-JC&t=414
             */
            String curNodePath = this.zooKeeper.create(
                    ROOT_PATH + "/" + this.lockName,
                    null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL
            );

            System.out.println("curNodePath = " + curNodePath);
            /**
             *
             *  get previous node, if null, then get lock successfully
             *  otherwise, monitor previous node
             */
            String preNodePath = this.getPreNode();
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

    /**
     *  delete ZK node
     *
     *  https://youtu.be/rw6jIVHirvQ?si=NS1cRbHlqaONNP8_&t=236
     */
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

    // local method
    private String getPreNode() {

        return null;
    }

}
