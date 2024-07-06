package com.yen.springWarehouse.service;

import com.yen.MyRLock.annotation.MyRLock;
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
            System.out.println("sleep 5 sec ...");
            Thread.sleep(1000 * 5);
        }
        return "success";
    }

}