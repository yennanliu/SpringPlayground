package com.yen.springBootAdvance3.service;

// https://www.youtube.com/watch?v=QL_JfKt-n4o&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=25

import org.springframework.stereotype.Service;

/** Async Service demo */

@Service
public class AsyncService {

    public void hello(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(">>> process data ....");
    }

}
