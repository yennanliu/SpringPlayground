package com.yen;

// https://www.youtube.com/watch?v=eKnWj_rDQO0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=29

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // will use zookeeper as service registry center
public class PaymentMain8004 {
    public static void main(String[] args) {

        SpringApplication.run(PaymentMain8004.class, args);
    }
}