package com.yen.SpringDistributionLock.service;

import com.yen.SpringDistributionLock.zookeeper.ZKClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *  ZK distribution demo
 *  https://youtu.be/dcmhIij3eNM?si=Cz-V8qGzXhWGK_lL&t=298
 */

@Service
public class StockService { // default : Singleton (@Scope("Singleton"))

    @Autowired
    private ZKClient zkClient;

    public void deduct() {

    }

}
