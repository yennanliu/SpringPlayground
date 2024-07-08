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
        taskExecutor.execute(task);
    }

    public Future<String> submitAsyncTask(Callable<String> task){
        return taskExecutor.submit(task);
    }

}
