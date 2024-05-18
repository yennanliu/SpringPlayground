package com.yen.SpringDistributionLock.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yen.SpringDistributionLock.mapper.StockMapper;
import com.yen.SpringDistributionLock.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

// https://youtu.be/O3ROb-vTd_o?si=XAw0KIHJCQC4ufHf&t=535


/**
 *  prototype (多例模式) make JVM local lock failed
 *  https://youtu.be/L7OFClDyWLs?si=kqVc5L_-Jv2GQU_G&t=119
 */
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS) // change to prototype (多例模式)
/**
 *  Transactional (事務性) make JVM local lock failed
 *  https://youtu.be/pD8bEeq9q_U?si=NzUuYN239o8J3N4Z&t=43
 */
//@Transactional
@Service
public class StockService { // default : Singleton (@Scope("Singleton"))

    @Autowired
    StockMapper stockMapper;

    private ReentrantLock lock = new ReentrantLock();

    public void deduct() {

        // lock
        lock.lock();
        int count = 0;
        try{
            // get record
            Stock stock = stockMapper.selectOne(new QueryWrapper<Stock>().eq("product_code", "prod-1"));
            if (stock != null && stock.getCount() > 0){
                count = stock.getCount();
                stock.setCount(count-1);
                stockMapper.updateById(stock);
            }
            System.out.println("Stock count = " + count);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("deduct op error : " + e);
        }finally {
            // unlock
            lock.unlock();
        }

    }

}
