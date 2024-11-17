package com.yen.config;

// https://www.youtube.com/watch?v=WeXWCwD4oX4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=30

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Config for RestTemplate usage */

@Configuration // necessary
public class ApplicationContextConfig {

    @Bean  // necessary
    @LoadBalanced // NOTE !!! necessary for cluster mode, enable RestTemplate load-balance capability
    public RestTemplate getRestTemplate(){

        return new RestTemplate();
    }

    /** Note :
     *
     *   1) "@Bean xxx" (above) is as same as (below)
     *   -> "application.yml <bean id = "" class = ""></bean>"
     */
}