package com.yen.SpringDistributionLock.controller;

import com.yen.SpringDistributionLock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("stock/deduct")
    public String deduct(){

        //stockService.deduct();
        //stockServiceWithSQL.deduct();
        //stockServiceWithPessimisticLock.deduct();
        //stockServiceWithOptimisticLock.deduct();
        stockServiceRedisOptimisticLock.deduct();

        return "Stock already deducted";
    }

}
