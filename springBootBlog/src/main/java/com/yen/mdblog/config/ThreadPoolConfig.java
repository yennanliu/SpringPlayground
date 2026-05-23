package com.yen.mdblog.config;

// https://blog.csdn.net/u010986241/article/details/138205146

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    int CORE_POOL_SIZE = 5;
    int MAX_POOL_SIZE = 10;
    int QUEUE_CAPACITY = 20;
    int KEEP_ALIVE_SECONDS = 30;
    String THREAD_NAME_PREFIX = "custom-thread-x-";

    @Bean(name="customThreadPool")
    public ThreadPoolTaskExecutor threadPoolExecutor(){

        System.out.println(">>>> ThreadPoolConfig init");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

}
