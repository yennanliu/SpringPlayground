package com.yen;

// https://www.youtube.com/watch?v=HQH90WprYgw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=9
// https://www.youtube.com/watch?v=ARfdin7xoZI&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=17
// https://www.youtube.com/watch?v=5o6W57mRJA0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=25

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient // register this service to eureka as client
@EnableDiscoveryClient
public class PaymentMain8002 {
    public static void main(String[] args) {

        SpringApplication.run(PaymentMain8002.class, args);
    }
}