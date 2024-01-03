package com.yen.SpringDistributionLock.controller;

import com.yen.SpringDistributionLock.service.*;
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

}
