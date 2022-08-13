package com.yen.springcloud;

// https://www.youtube.com/watch?v=BFYHIlX_Sts&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=51

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {

        SpringApplication.run(PaymentHystrixMain8001.class, args);
    }

}
