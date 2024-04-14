package com.yen.SpotifyPlayList.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Enable multi thread in spring boot */

@Configuration
public class AppConfig {

    private final int THREAD_COUNT = 5;

    @Bean
    public ExecutorService executorService() {
        // Create a fixed thread pool with 5 threads
        return Executors.newFixedThreadPool(THREAD_COUNT);
    }
}

