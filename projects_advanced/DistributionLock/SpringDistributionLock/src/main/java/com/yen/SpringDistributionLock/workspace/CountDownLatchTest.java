package com.yen.SpringDistributionLock.workspace;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 *   JUC CountDownLatch demo
 *      - purpose multiple event wait for one specific event
 *      - only work for single node
 *      - https://youtu.be/qsypEWwBLR8?si=gP-U00lwpdKk6zwf&t=298
 *
 */
public class CountDownLatchTest {

    /**
     *   main thread as leader
     *
     *   other threads as student
     *
     */
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 6; i++){

            new Thread(() -> {

                try{
                    System.out.println(Thread.currentThread().getName() +  " ready to leave ...");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));

                    System.out.println(Thread.currentThread().getName() +  " leave room !!!");
                    countDownLatch.countDown();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }, i + " member").start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " leader lock door !!!");

    }

}
