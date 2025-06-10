package EmployeeSystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        log.info("Default AsyncExecutor init ...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Default-Async-");
        executor.initialize();
        return executor;
    }

    /**
     * Dedicated thread pool for email service operations
     * This ensures email processing is isolated and has dedicated resources
     */
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        log.info("Email AsyncExecutor init ...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Core pool size - always available threads
        executor.setCorePoolSize(3);
        
        // Maximum pool size - threads created when queue is full
        executor.setMaxPoolSize(8);
        
        // Queue capacity - number of tasks that can be queued
        executor.setQueueCapacity(50);
        
        // Thread naming pattern for easy identification in logs
        executor.setThreadNamePrefix("Email-Service-");
        
        // Keep alive time for idle threads (in seconds)
        executor.setKeepAliveSeconds(60);
        
        // Allow core threads to timeout
        executor.setAllowCoreThreadTimeOut(true);
        
        // Rejection policy when pool and queue are full
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // Wait for tasks to finish before shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        
        log.info("Email Task Executor configured with core pool size: {}, max pool size: {}, queue capacity: {}", 
                executor.getCorePoolSize(), executor.getMaxPoolSize(), 50);
        
        return executor;
    }

    /**
     * Dedicated thread pool for notification operations (if needed for other notifications)
     */
    @Bean(name = "notificationTaskExecutor")
    public Executor notificationTaskExecutor() {
        log.info("Notification AsyncExecutor init ...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Notification-Service-");
        executor.setKeepAliveSeconds(30);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}