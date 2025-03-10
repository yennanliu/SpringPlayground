package com.yen.springcloud.config;

// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13
// https://www.youtube.com/watch?v=uLnMgNai8nc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=23
// https://www.youtube.com/watch?v=m_MHwd3Dls4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=43

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Config for RestTemplate usage */

@Configuration // necessary
public class ApplicationContextConfig {

    @Bean  // necessary
    // comment @LoadBalanced, in order to make sure the app is using our own round-robin implementation code
    // https://youtu.be/m_MHwd3Dls4?t=180
    //@LoadBalanced // NOTE !!! necessary for cluster mode, enable RestTemplate load-balance capability
    public RestTemplate getRestTemplate(){

        return new RestTemplate();
    }

    /** Note :
     *
     *   1) "@Bean xxx" (above) is as same as (below)
     *   -> "application.yml <bean id = "" class = ""></bean>"
     */
}
