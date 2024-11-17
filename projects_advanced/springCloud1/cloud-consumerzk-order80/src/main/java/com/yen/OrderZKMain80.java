package com.yen;

// https://www.youtube.com/watch?v=WeXWCwD4oX4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=30

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // will use zookeeper as service registry center
public class OrderZKMain80 {
    public static void main(String[] args) {

        SpringApplication.run(OrderZKMain80.class, args);
    }

}