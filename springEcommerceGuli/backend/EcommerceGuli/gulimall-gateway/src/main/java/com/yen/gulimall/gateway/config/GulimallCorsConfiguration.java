package com.yen.gulimall.gateway.config;

/**
 *  Config enable CORS
 *      - https://youtu.be/VNP6inKmw5I?t=526
 *      - so CORS is enabled when request sent to API GATEWAY
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GulimallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        /**
         *   NOTE:
         *      - we use "responsive" programming, so use below pkg (reactive)
         *      - org.springframework.web.cors.reactive
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // /** : do CORS for all paths

        // 1) config CORS
        corsConfiguration.addAllowedHeader("*"); // "*" : allow everything
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }

}
