package com.yen.springThreadPool.service.impl;

// https://youtu.be/c134eGL062g?t=1565

import com.yen.springThreadPool.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class HelloServiceImpl implements HelloService {

    /** NOTE HERE !!!
     *
     *  -> we "autowired" ThreadPoolExecutor here for multi-thread implementation
     *
     */
    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public String printNumMultiThread() {

        Integer myInt = 30;
        List<Integer> myList = new ArrayList<>();
        while (myInt > 0){
            myList.add(myInt);
            myInt -= 1;
        }

        /**
         *  multi-thread implementation here
         *
         *  https://youtu.be/c134eGL062g?t=1597
         */
        CompletableFuture.runAsync( () -> {

            for (int x : myList){
                System.out.println(
                        " Thread name = " + Thread.currentThread().getName() +
                        " Thread id = " + Thread.currentThread().getId() +
                        " x = "  + x);
            }
        });

        return "printNumMultiThread";
    }

}
