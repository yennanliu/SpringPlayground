package com.yen.springWarehouse.service;

import com.yen.MyRLock.annotation.MyRLock;
import com.yen.MyRLock.annotation.MyRLockKey;
import com.yen.springWarehouse.bean.Product;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

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
            Thread.sleep(1000 * 5);
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

}