package com.yen.springBootAdvance3.service;

// https://www.youtube.com/watch?v=QL_JfKt-n4o&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=25

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** Async Service demo */

@Service
public class AsyncService {

    /** NOTE !!!
     *
     *    1) via @Async, we let Spring know this is an async method
     *    2) Spring will open the other thread for the is method
     *    3) have to add @EnableAsync at app program ( SpringBootAdvance3Application in this project)
     */
    @Async
    public void hello(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(">>> process data ....");
    }

}
