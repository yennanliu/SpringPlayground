package com.yen.springWarehouse.service;

// https://blog.csdn.net/u010986241/article/details/138205146

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class AsyncTestService {

    @Autowired
    @Qualifier("customThreadPool") // https://kucw.io/doc/springboot/8/ : specify bean name, to avoid classes with same parent type, but different name when DI
    private ThreadPoolTaskExecutor taskExecutor;

    public void executeAsyncTask(Runnable task){

        /** V1 */
//        System.out.println("--> Thread name : " + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
//        taskExecutor.execute(task);

        /** V2 */
        /**
         * (gpt)
         * 
         * Ensure that your print statements (Thread.currentThread().getName() and
         * Thread.currentThread().getId()) are placed within the
         * execution context of tasks submitted to ThreadPoolTaskExecutor.
         * This will accurately reflect the thread names from the
         * thread pool configuration (custom-thread-x-1, etc.)
         * rather than the container-managed thread names (http-nio-7777-exec-1).
         * This adjustment should align your logging with the threads managed
         * by your custom thread pool in Spring boot.
         *
         */
         taskExecutor.execute(() -> {
            System.out.println("--> Thread name : " + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
            task.run();
        });
    }

    public Future<String> submitAsyncTask(Callable<String> task){

        /** V1 */
//        System.out.println("--> Thread name : " + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
//        return taskExecutor.submit(task);


        /** V2 */
        return taskExecutor.submit(() -> {
            System.out.println("--> Thread name : " + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
            return task.call();
        });

    }

    public void printThreadInfo(){
        System.out.println("--> Thread name : " + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
    }

}
