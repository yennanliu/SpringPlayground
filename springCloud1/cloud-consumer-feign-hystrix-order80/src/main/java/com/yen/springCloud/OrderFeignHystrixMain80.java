package com.yen.springCloud;

// https://www.youtube.com/watch?v=AO0iGzLF-M0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=53
// https://www.youtube.com/watch?v=NGhYY67j1kc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=56

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // NOTE !!! we HAVE to enable Feign client, so spring boot can use it
@EnableHystrix // enable hystrix
public class OrderFeignHystrixMain80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderFeignHystrixMain80.class, args);
    }

}
