package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.mapper.StockMapper;
import com.yen.SpringDistributionLock.pojo.Stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Pessimistic lock (悲觀鎖) demo
 *
 *   - https://youtu.be/tIIOSs4Wd-0?si=JlNWhGGem6mS35YD&t=369
 *   - row lock (行級別鎖) for multi thread data safety
 *   - select ... for update
 *
 *   - pros
 *
 *   - cons
 *      - performance not that good
 *      - deadlock : make sure lock ordering when lock multiple records
 *      - storage op needs to be consistent (庫存操作需統ㄧ)
 *         - e.g.:
 *            a) "select ... from" VS b) "select from ... for update"
 *            不能有些用a) 有些用b)
 */

@Service
public class StockServiceWithPessimisticLock { // default : Singleton (@Scope("Singleton"))

    @Autowired
    StockMapper stockMapper;

    private ReentrantLock lock = new ReentrantLock();

    @Transactional
    public void deduct() {

        // step 1) get storage count and lock record
        List<Stock> stockList = stockMapper.queryStock("prod-1");
        // get 1st product (stock) for following op
        Stock stock = stockList.get(0);

        // step 2) check if count is enough (>0)
        if (stock != null && stock.getCount() > 0){

            // step 3) deduct storage
            stock.setCount(stock.getCount()-1);
            stockMapper.updateById(stock);
        }
    }

}
