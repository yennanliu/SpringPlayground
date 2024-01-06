package com.yen.SpringDistributionLock.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZK client test (org.apache.zookeeper)
 *
 *
 * <p>
 *  - https://youtu.be/LTiRPXieav4?si=XCZiKKPk47OR1cYu&t=191
 *  - https://youtu.be/zvYXU_oxZ08?si=F_b72ozRuYsWLlO7&t=23
 *  - https://youtu.be/Y-1ra744pKk?si=GgEbtPnQ4PinPM_8&t=15
 */
public class ZkTest {

    public static void main(String[] args) throws InterruptedException, KeeperException {

        ZooKeeper zooKeeper = null;

        // https://youtu.be/LTiRPXieav4?si=nKlZMXHNQvV5SfT5&t=574
        // use CountDownLatch to avoid doing op BEFORE receive ZK connection
        CountDownLatch cdl = new CountDownLatch(1);

        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                    Event.KeeperState state = event.getState();
                    if (Event.KeeperState.SyncConnected.equals(state)){
                        System.out.println(">>> try to get ZK connection ..." + event);
                    }else if (Event.KeeperState.Closed.equals(state)){
                        System.out.println(">>> close ZK connection ..." + event);
                    }
                    cdl.countDown(); // amount -= 1
                }
            });


            cdl.await(); // will block (await) before amount == 0
            System.out.println("--> some operation ...");

            // create permanent node
            zooKeeper.create("/yen/test_1", "hello zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            // create Ephemeral node
            zooKeeper.create("/yen/test_2", "hello Ephemeral zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            // create permanent serialization node
            zooKeeper.create("/yen/test_3", "hello permanent serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//            zooKeeper.create("/yen/test_3", "hello permanent serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
//            zooKeeper.create("/yen/test_3", "hello permanent serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

            // create Ephemeral serialization node
            zooKeeper.create("/yen/test_4", "hello EPHEMERAL serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zooKeeper.create("/yen/test_4", "hello EPHEMERAL serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            zooKeeper.create("/yen/test_4", "hello EPHEMERAL serialization zk !!!".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // check if node exists
            String path = "/aa/cc"; //"/yen/test_1";
            Stat stat = zooKeeper.exists(path, false);
            if (stat != null){
                System.out.println("Node exists : " + path);
            }else{
                System.out.println("Node NOT exists : " + path);
            }

            // get node data (content)
            byte[] data = zooKeeper.getData(path, false, stat);
            System.out.println("Node data = " + new String(data));

            // check current node's sub node
            List<String> children = zooKeeper.getChildren(path, false, stat);
            System.out.println("Sub node list = " + children);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        }

    }

}
