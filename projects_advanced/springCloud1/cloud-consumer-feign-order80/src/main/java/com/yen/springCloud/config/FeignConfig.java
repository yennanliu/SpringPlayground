package com.yen.springCloud.config;

// https://www.youtube.com/watch?v=8PVkp8jc-N8&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=47

/** enable feign client logging */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
