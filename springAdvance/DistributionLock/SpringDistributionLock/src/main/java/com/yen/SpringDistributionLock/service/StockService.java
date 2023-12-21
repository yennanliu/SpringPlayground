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
            /**
             *   Ideas for solving data inconsistency in cluster deployment (集群部署)
             *
             *   Idea 1) solve with SQL
             *     -> update db_stock set count = count - 1 where product_code = 'prod-1' and count >= 1;
             *     -> 表級鎖 (鎖住整個表)
             *
             *   Idea 2) 悲觀鎖
             *     -> select ... for update
             *     -> https://youtu.be/HyD7E8WkJhI?si=oTQgzl92MElMfjbX&t=38
             *     -> 如果想要行級鎖(只鎖住選定資料) 必須同時滿足以下二個條件:
             *         -> 1) 鎖的查詢/更新條件必須是 index (索引)
             *         -> 2) 查詢/更新條件必須是具體值 (example : 不可以是 模糊查詢, like ...)
             *
             *
             *   Idea 3) 樂觀鎖
             *      -> 時間戳+版本號
             *      -> CAS algorithm
             *          - compare and swap
             *          - (if old version == new version, then update or abort) similar to password change
             *
             *          ```pseudocode
             *          if old version == new version:
             *             update;
             *          else:
             *             abort;
             *          ```
             *
             *      -> https://youtu.be/y7blICVJ2i0?si=qURQCRgzyT9SPTHd
             */
            // 1) get stock amount
            // get record
            Stock stock = stockMapper.selectOne(new QueryWrapper<Stock>().eq("product_code", "prod-1"));
            // 2) check if stock is enough
            if (stock != null && stock.getCount() > 0){
                count = stock.getCount();
                stock.setCount(count-1);
                // 3) update stock to DB
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
