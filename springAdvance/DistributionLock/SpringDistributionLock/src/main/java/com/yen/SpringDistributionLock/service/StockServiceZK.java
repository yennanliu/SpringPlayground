//package com.yen.SpringDistributionLock.service;
//
//import com.yen.SpringDistributionLock.zookeeper.ZKClient;
//import com.yen.SpringDistributionLock.zookeeper.ZKDistributionLock;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class StockServiceZK {
//
//    @Autowired
//    private ZKClient zkClient;
//
//    public void deduct(){
//
//        // get lock
//        ZKDistributionLock lock = zkClient.getLock("lock");
//        // lock
//        lock.lock();
//
//        try{
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            // unlock
//            lock.unlock();
//        }
//
//    }
//}
