package com.yen.springcloud;

// https://www.youtube.com/watch?v=8Fe85klW2TM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=70

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GateWayApp9527 {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApp9527.class, args);
    }
}
