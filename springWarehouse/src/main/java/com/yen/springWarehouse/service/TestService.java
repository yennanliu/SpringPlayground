package com.yen.springWarehouse.service;

import com.yen.MyRLock.annotation.MyRLock;
import com.yen.MyRLock.annotation.MyRLockKey;
import com.yen.springWarehouse.bean.Product;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.yen.MyRLock.model.LockTimeoutStrategy.NO_OPERATION;
import static com.yen.MyRLock.model.LockType.Reentrant;
import static com.yen.MyRLock.model.ReleaseTimeoutStrategy.FAIL_FAST;

@Service
public class TestService {

    //@MyRLock(name = "MyRLock-1", lockType = Reentrant, waitTime = Long.MAX_VALUE, leaseTime = 1000L, keys = {}, lockTimeoutStrategy = NO_OPERATION, customLockTimeoutStrategy = "", releaseTimeoutStrategy = FAIL_FAST, customReleaseTimeoutStrategy = "")
    //@MyRLock(keys = {"#userId"})
    //@MyRLock()
    @MyRLock(waitTime = Long.MAX_VALUE)
    public String getValue(String param) throws Exception {
        System.out.println("param = " + param);
        if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
            System.out.println("(getValue) sleep 5 sec ...");
            Thread.sleep(1000 * 5); // 5 sec
        }
        return "success";
    }

    @MyRLock(keys = {"#userId"})
    public String getValue(String userId, @MyRLockKey int id) throws Exception {
        System.out.println("userId = " + userId + ", id = " + id);
        System.out.println("(getValue with userId, id) sleep 5 sec ...");
        Thread.sleep(1000 * 5);
        return "success";
    }

    @MyRLock(keys = {"#user.name", "user.id"})
    public String getValue(Product product) throws Exception {
        System.out.println("product = " + product);
        System.out.println("(getValue product) sleep 5 sec ...");
        Thread.sleep(1000 * 5);
        return "success";
    }

    public String getValueWithMultiThread(String param) throws Exception {

        int THREAD_COUNT = 10; // Number of threads

        System.out.println("getValueWithMultiThread start ...");

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        CountDownLatch latch = new CountDownLatch(THREAD_COUNT); // Countdown latch to wait for all threads to finish

        IntStream.range(0, 30).forEach(i-> executorService.submit(() -> {
            try {
                String result = this.getValue(param);
                System.err.println("---> Thread :[" + Thread.currentThread().getName() + "] get result =>" + result + " " + new Date().toString());
            } catch (Exception e) {
                System.out.println("thread execution error");
                e.printStackTrace();
            }finally{
                latch.countDown(); // Count down the latch after thread completes
            }
        }));

//        System.out.println("param = " + param);
//        if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
//            System.out.println("(getValue) sleep 5 sec ...");
//            Thread.sleep(1000 * 5);
//        }

        // Wait until all threads complete
        latch.await();

        System.out.println("getValueWithMultiThread end ...");
        return "success";
    }

}