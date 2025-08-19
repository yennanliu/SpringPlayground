package com.yen.mdblog.config;

// https://youtu.be/xUux3Ycjh7U?si=ymKyfKFYDxxjpT4g&t=2113
// custom webflux config

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

public class MyWebFluxConfig {

    @Bean
    public WebFluxConfigurer webFluxConfigurer(){

        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // WebFluxConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOrigins("localhost");
            }
        };

    }

}