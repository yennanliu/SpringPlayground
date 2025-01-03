package com.yen.SpringDistributionLock.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

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
    private String lockName;
    private ZooKeeper zooKeeper;

    private String currentNodePath;

    private static final String ROOT_PATH = "/locks";

    // constructor
    public ZKReentrantLock(ZooKeeper zooKeeper, String lockName) {

        // check if root path exists; if not, create
        try {
            if (zooKeeper.exists(ROOT_PATH, false) == null) {
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
     * create ZK node, if can't create, keep retrying
     * https://youtu.be/rw6jIVHirvQ?si=fbGVH9knZ38uPFa4
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
            System.out.println("(tryLock) flag = " + flag);
            if (flag != null && flag > 0){
                THREAD_LOCAL.set(flag + 1);
                return true;
            }

            /**
             *  NOTE !!!
             *
             *   - Here we create blocking lock via ZK ephemeral serialization node (臨時序列化節點)
             *   - 阻塞鎖 利用 `ZK 臨時序列化節點`
             *   - "-" is for below getPreNode() method
             *
             *   - https://youtu.be/yAp8mbhxWlM?si=7B48O3JvfV43e-JC&t=414
             */
            this.currentNodePath = this.zooKeeper.create(
                    ROOT_PATH + "/" + this.lockName + "-",
                    null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL
            );

            System.out.println("currentNodePath = " + this.currentNodePath);
            /**
             *
             *  get previous node, if null, then get lock successfully
             *  otherwise, monitor previous node
             *
             *  - https://youtu.be/lrlOo4ouzHY?si=Q2IcmKzi4kJrN1NA&t=113
             */
            String preNodePath = this.getPreNode();
            // check if preNodePath is null
            if (preNodePath != null) {
                /**
                 *  implement block via blocking lock
                 *
                 */
                CountDownLatch cdl = new CountDownLatch(1);

                /**
                 *  NOTE !!!
                 *     check if ZK previous node existed
                 *     since getPreNode() is NOT ATOM (原子性操作)
                 *
                 *     - https://youtu.be/lrlOo4ouzHY?si=fmXX2boUnlH2QMSk&t=1145
                 */
                if (this.zooKeeper.exists(ROOT_PATH + "/" + preNodePath, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        cdl.countDown();
                    }
                }) == null) {

                    /**
                     *  record thread get Reentrant lock
                     *
                     *  - https://youtu.be/0C-c0QEIP3A?si=4qJonm7I8VCakRP7&t=414
                     */
                    THREAD_LOCAL.set(1);

                    return true;
                }
                cdl.await();
            }

            THREAD_LOCAL.set(1);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * delete ZK node
     * <p>
     * https://youtu.be/rw6jIVHirvQ?si=NS1cRbHlqaONNP8_&t=236
     */
    @Override
    public void unlock() {

        try {

            /**
             *  deduct lock val with 1 ( val -=1) when unlock
             *
             *  https://youtu.be/0C-c0QEIP3A?si=hImzu1hd6IHY7kqa&t=443
             */
            THREAD_LOCAL.set(THREAD_LOCAL.get() - 1);
            // if local val == 0, release lock
            if (THREAD_LOCAL.get() == 0){
                this.zooKeeper.delete(this.currentNodePath, -1);
            }

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

    /**
     * Method get previous node in ZK
     * <p>
     * https://youtu.be/lrlOo4ouzHY?si=es2MC0ehpCaVLqYC&t=225
     */
    private String getPreNode() {

        // get all nodes under root path
        try {

            // get all nodes under root path
            List<String> children = this.zooKeeper.getChildren(ROOT_PATH, false);

            // edge case : children is null
            if (CollectionUtils.isEmpty(children)) {
                throw new IllegalMonitorStateException("(getPreNode) error : children is null");
            }

            // get (previous) nodes has same resources as current node
            List<String> nodes = children.stream().filter(node -> {
                /**
                 *  example :
                 *      if current node = /locks/lock-
                 *      and we have children as below:
                 *           /locks/lock-
                 *           /locks/lock-
                 *           /locks/lockx-
                 *           /locks/locky
                 *           /locks/lockxy-
                 *           ...
                 *      so we should collect /locks/lock-, /locks/lock-
                 */
                return StringUtils.startsWith(node, lockName + "-");
            }).collect(Collectors.toList());

            // edge case : nodes is null
            if (CollectionUtils.isEmpty(nodes)) {
                throw new IllegalMonitorStateException("(getPreNode) error : nodes is null");
            }

            // ordering
            Collections.sort(nodes);

            // get current node name
            // example :  if path = /aa/bb, then we get /bb
            String curNode = StringUtils.substringAfterLast(currentNodePath, "/");
            /**
             *   get current node subscript (下標)
             *   if subscript == 0, get current lock
             *   else, monitor previous node
             */
            int index = Collections.binarySearch(nodes, curNode);
            if (index < 0) {
                throw new IllegalMonitorStateException("(getPreNode) error : index smaller than 0");
            } else if (index > 0) {
                return nodes.get(index - 1); // else, monitor previous node
            }
            // if subscript == 0, get current lock -> return null
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalMonitorStateException("(getPreNode) error : Invalid op");
        }
    }


}
