package com.yen.SpringDistributionLock.controller;

import com.yen.SpringDistributionLock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("stock/deduct")
    public String deduct(){
        stockService.deduct();
        return "Stock already deducted";
    }

}
