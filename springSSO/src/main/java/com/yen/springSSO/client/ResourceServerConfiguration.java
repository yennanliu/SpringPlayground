package com.yen.springSSO.client;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/java/com/wudimanong/authserver/client/ResourceServerConfiguration.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceServerConfiguration {

    @Bean
    ResourceServerFallbackFactory resourceServerFallbackFactory() {

        return new ResourceServerFallbackFactory();
    }

}