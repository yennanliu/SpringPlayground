package com.yen.SpringDistributionLock.workspace;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * JUC Semaphore demo
 *
 *   6 car compete 3 resources with Semaphore
 *
 *   - purpose : 限流, resource control
 *
 *   - https://youtu.be/LHd8_ATmBD8?si=aRS1dk6uBj-kgez9&t=120
 */
public class SemaphoreTest {

    public static void main(String[] args) {

        // init resource with 3 amount
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {

                    // get resource
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " get parking space !");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    System.out.println("--> " + Thread.currentThread().getName() + " leave parking lot.");

                    // release resource
                    semaphore.release();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, i + " th car").start();
        }

    }

}
