package com.yen.SpringDistributionLock.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;

/**
 * ZK client
 * <p>
 * https://youtu.be/dcmhIij3eNM?si=botaWUkhGIXVimUX&t=34
 */
@Component
public class ZKClient {

    private ZooKeeper zooKeeper;

    /**
     * @PostConstruct :
     * - will run after No args constructor is init (when spring boot scan pkg, can init container)
     * - https://youtu.be/dcmhIij3eNM?si=FKF7YAho4jogK1Vb&t=95
     */
    // get ZK connection
    @PostConstruct
    public void init() {

        // get ZK connection, so app will be able to connect to ZK when launch
        System.out.println("get ZK connection");
        CountDownLatch cdl = new CountDownLatch(1);

        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 30000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                    Event.KeeperState state = event.getState();
                    if (Event.KeeperState.SyncConnected.equals(state) && Event.EventType.None.equals(event.getState())) {
                        System.out.println(">>> try to get ZK connection ..." + event);
                        cdl.countDown(); // amount -= 1
                    } else if (Event.KeeperState.Closed.equals(state)) {
                        System.out.println(">>> close ZK connection ..." + event);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("get ZK connection error : " + e);
            e.printStackTrace();
        }

    }


    /**
     * @PreDestroy :
     * - will run before spring container is destroyed
     * - https://youtu.be/dcmhIij3eNM?si=fBAs28bo6UsCJaup&t=178
     */
    // close ZK connection
    @PreDestroy
    public void destroy() {

        // get ZK connection, so app will be able to connect to ZK when launch
        System.out.println("release ZK connection");

        try{
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        }catch (Exception e){
            System.out.println("release ZK connection error : " + e);
            e.printStackTrace();
        }
    }

    public ZKDistributionLock getLock(ZooKeeper zooKeeper, String lockName){

        return new ZKDistributionLock(zooKeeper, lockName);
    }

}
