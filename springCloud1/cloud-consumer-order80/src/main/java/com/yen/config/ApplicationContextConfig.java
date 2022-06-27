package com.yen.config;

// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Config for RestTemplate usage */

@Configuration // necessary
public class ApplicationContextConfig {

    @Bean  // necessary
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /** Note :
     *
     *   1) "@Bean xxx" (above) is as same as (below)
     *   -> "application.yml <bean id = "" class = ""></bean>"
     *
     */

}
