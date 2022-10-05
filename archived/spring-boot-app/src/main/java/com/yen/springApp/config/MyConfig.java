package com.yen.springApp.config;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=84

import com.yen.hello.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public HelloService helloService(){
        HelloService helloService = new HelloService();
        return helloService;
    }

}
