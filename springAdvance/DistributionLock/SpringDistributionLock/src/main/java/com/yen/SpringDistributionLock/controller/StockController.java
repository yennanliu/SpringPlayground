package com.yen.SpringDistributionLock.controller;

import com.yen.SpringDistributionLock.service.*;
import com.yen.SpringDistributionLock.zookeeper.ZKDistributionLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Autowired
    StockService stockService;

    @Autowired
    StockServiceWithSQL stockServiceWithSQL;

    @Autowired
    StockServiceWithPessimisticLock stockServiceWithPessimisticLock;

    @Autowired
    StockServiceWithOptimisticLock stockServiceWithOptimisticLock;

    @Autowired
    StockServiceRedisOptimisticLock stockServiceRedisOptimisticLock;

    @Autowired
    StockServiceRedisDistributionLock stockServiceRedisDistributionLock;

    @Autowired
    StockServiceRedisDistributionLockLua stockServiceRedisDistributionLockLua;

    @Autowired
    StockServiceRedisReentrantLockLua stockServiceRedisReentrantLockLua;

    @Autowired
    StockServiceRedisson stockServiceRedisson;

    @Autowired
    StockServiceRedissonFairLock stockServiceRedissonFairLock;

    @Autowired
    StockServiceRedissonReadWriteLock stockServiceRedissonReadWriteLock;

    @Autowired
    StockServiceSemaphore stockServiceSemaphore;

    @Autowired
    StockServiceCountDownLatch stockServiceCountDownLatch;

    @Autowired
    StockServiceZK stockServiceZK;

    @Autowired
    StockServiceZKCurator stockServiceZKCurator;

    @GetMapping("stock/deduct")
    public String deduct(){

        //stockService.deduct();
        //stockServiceWithSQL.deduct();
        //stockServiceWithPessimisticLock.deduct();
        //stockServiceWithOptimisticLock.deduct();
        //stockServiceRedisOptimisticLock.deduct();
        //stockServiceRedisDistributionLock.deduct();
        //stockServiceRedisDistributionLockLua.deduct();
        //stockServiceRedisReentrantLockLua.deduct();
        stockServiceRedisson.deduct();

        return "Stock already deducted";
    }

    /**
     *  Endpoint for testing Redisson fair lock
     *   - https://youtu.be/_7VL1DUlq1Q?si=avP4D0PkJrzikFfl&t=288
     */
    @GetMapping("test/fair/lock/{id}")
    public String testFairLock(@PathVariable("id") Long id){

        stockServiceRedissonFairLock.testFairLock(id);
        return "Test Fair Lock";
    }


    /**
     *  Endpoint for testing Redisson ReadLock
     *   - https://youtu.be/T___uj6PolA?si=dmrd6x4LPzBGzHWA&t=377
     */
    @GetMapping("test/read/lock")
    public String testReadLock(){

        stockServiceRedissonReadWriteLock.testReadLock();
        return "Test Read Lock";
    }


    /**
     *  Endpoint for testing Redisson write Lock
     *   - https://youtu.be/T___uj6PolA?si=2NaBUHcdLzC6fuTD&t=403
     */
    @GetMapping("test/write/lock")
    public String testWriteLock(){

        stockServiceRedissonReadWriteLock.testWriteLock();
        return "Test Write Lock";
    }

    /**
     *  Test Distribution Semaphore with redis
     *
     *   - https://youtu.be/LHd8_ATmBD8?si=-yzFQ85oGTB0GOZN&t=571
     */
    @GetMapping("test/semaphore")
    public String testSemaphore(){

        stockServiceSemaphore.testSemaphore();
        return "Test Semaphore";
    }

    /**
     *  CountDownLatch
     *
     *  https://youtu.be/qsypEWwBLR8?si=8ZMv8CEmMWuPmbmS&t=768
     */
    @GetMapping("test/latch")
    public String testLatch(){

        stockServiceCountDownLatch.testLatch();
        return "CountDownLatch  - Leader close door !!!";
    }

    @GetMapping("test/countdown")
    public String testCountDown(){

        stockServiceCountDownLatch.testCountDown();
        return "CountDownLatch  - student leave room ... ";
    }

    @GetMapping("test/zk")
    public String testZKLock(){

        stockServiceZK.deduct();
        return "stockServiceZK  - deduct run ... ";
    }

    @GetMapping("test/zk_curator")
    public String testZKCuratorLock(){

        stockServiceZKCurator.deduct();
        return "stockServiceZKCurator  - deduct run ... ";
    }

    /**
     *  Test ZK Curator - InterProcessReadWriteMutex : 可重入讀寫鎖
     *
     *  https://youtu.be/LCiEhaqyJ38?si=HyOqykBq2cA9CFhv&t=82
     */
    @GetMapping("test/zk_curator/read/lock")
    public String testZKCuratorReadLock(){

        stockServiceZKCurator.testZKCuratorReadLock();
        return "Test ZK Curator Read Lock";
    }

    @GetMapping("test/zk_curator/write/lock")
    public String testZKCuratorWriteLock(){

        stockServiceZKCurator.testZKCuratorWriteLock();
        return "Test ZK Curator Write Lock";
    }

}
