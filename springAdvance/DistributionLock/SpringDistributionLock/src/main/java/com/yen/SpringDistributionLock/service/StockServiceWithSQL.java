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
 *  Pure SQL deal with thread safety demo
 *
 *   - table lock (表級別鎖) for multi thread data safety
 *
 */


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
public class StockServiceWithSQL { // default : Singleton (@Scope("Singleton"))

    @Autowired
    StockMapper stockMapper;

    private ReentrantLock lock = new ReentrantLock();

    public void deduct() {

        // lock
        lock.lock();
        int count = 0;
        try{
            /**
             *   Ideas for solving data inconsistency in cluster deployment (集群部署)
             *
             *   Idea 1) solve with SQL (pure SQL)
             *     // https://youtu.be/vOQqh8WucVA?si=LvA2QBVIQ_mkxjT0&t=315
             *     -> update db_stock set count = count - 1 where product_code = 'prod-1' and count >= 1;
             *
             *     -> pros
             *
             *     -> cons
             *          1. 鎖的範圍
             *          2. ㄧ個商品有多個庫存紀錄(same product id) if multiple records with same "product id", then all records will be locked, affected
             *          3. CAN'T track record change history
             *
             */

            // Idea 1) solve with SQL
            stockMapper.updateStock("prod-1", 1);
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
