package com.yen.springMultiThread.config;

// https://github.com/swathisprasad/spring-boot-completable-future/blob/master/src/main/java/com/techshard/future/AsyncConfiguration.java

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "taskExecutor") // TODO : verify if name is necessary
    public Executor taskExecutor(){

        log.info("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // set up param
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("CarThread-");
        executor.initialize();
        return executor;
    }

}
