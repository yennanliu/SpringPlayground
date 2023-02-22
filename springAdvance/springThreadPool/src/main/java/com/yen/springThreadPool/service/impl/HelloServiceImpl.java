package com.yen.springThreadPool.service.impl;

// https://youtu.be/c134eGL062g?t=1565

import com.yen.springThreadPool.service.HelloService;
import org.apache.tomcat.jni.Time;
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
        //List<Integer> myRes1 = new ArrayList<>();
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
        }, executor);


        /**
         *  if HAVE TO WAIT specific result completed before further execution
         *
         *  https://youtu.be/c134eGL062g?t=2325
         */
        String res1 = this.myTask();
        // TODO : fix this
        //CompletableFuture.allOf(res1);

        return "printNumMultiThread";
    }

    @Override
    public void slowHello() throws InterruptedException {
        System.out.println(">>> SlowHello start" + " Thread id = " + Thread.currentThread().getId());
        Thread.sleep(3000L);
        System.out.println(">>> SlowHello end" + " Thread id = " + Thread.currentThread().getId());
    }

    public String myTask(){
        return "hello";
    }

}
