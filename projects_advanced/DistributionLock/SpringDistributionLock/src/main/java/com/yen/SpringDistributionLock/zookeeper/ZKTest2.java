package com.yen.SpringDistributionLock.zookeeper;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZK client test 2 (org.apache.zookeeper)
 *
 *  - https://youtu.be/-c8GrB9nXiU?si=fWgNCrUcC9xb616U&t=86
 *
 */
public class ZKTest2 {


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

                    // https://youtu.be/-c8GrB9nXiU?si=fLGbrDjOskAvk5Kv&t=340
                    // only Event.EventType.None == null, then we recognize as "get ZK connection" event
                    if (Event.KeeperState.SyncConnected.equals(state) && Event.EventType.None.equals(event.getState())){
                        System.out.println(">>> try to get ZK connection ..." + event);
                    }else if (Event.KeeperState.Closed.equals(state)){
                        System.out.println(">>> close ZK connection ..." + event);
                    }else{
                        System.out.println("ZK event  ..." + event);
                    }
                    cdl.countDown(); // amount -= 1
                }
            });


            cdl.await(); // will block (await) before amount == 0
            System.out.println("--> some operation ...");

            // check if node exists  (zk cmd : stat)
            String path = "/aa/cc"; //"/yen/test_1";
            Stat stat = zooKeeper.exists(path, false);
            if (stat != null){
                System.out.println("Node exists : " + path);
            }else{
                System.out.println("Node NOT exists : " + path);
            }

            // check current node's sub node (zk cmd : ls)
            /**
             *  watch = true
             *   -> monitor ZK node
             *
             *   - https://youtu.be/-c8GrB9nXiU?si=pdEoMektz-TNaFEe&t=93
             *
             *   keep running monitor : use Watcher (or monitor will ONLY run ONCE)
             *
             *   - https://youtu.be/-c8GrB9nXiU?si=cucsMF6zEF4ve9mn&t=516
             *
             */
            //List<String> children = zooKeeper.getChildren(path, true, stat);
            List<String> children = zooKeeper.getChildren(path, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(">>> sub node changed ...." + event);
                }
            });
            System.out.println("Sub node list = " + children);

            /**
             *  in order to keep monitoring, need to keep main app running
             *   - https://youtu.be/-c8GrB9nXiU?si=bUIgiJu4ufuVSKXH&t=152
             */
            System.in.read();


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
