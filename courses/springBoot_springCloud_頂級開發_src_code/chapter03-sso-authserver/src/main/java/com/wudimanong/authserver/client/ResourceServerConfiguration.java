package com.wudimanong.authserver.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangqiao
 */
@Configuration
public class ResourceServerConfiguration {

    @Bean
    ResourceServerFallbackFactory resourceServerFallbackFactory() {
        return new ResourceServerFallbackFactory();
    }
}
