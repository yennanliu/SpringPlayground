package com.yen;

// https://www.youtube.com/watch?v=3yGY9RxCtOc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=17
// https://www.youtube.com/watch?v=ukm7jep5doc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=20

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // declare this is a eureka server (registry server)
public class EurekaMain7002 {
    public static void main(String[] args) {

        SpringApplication.run(EurekaMain7002.class, args);
    }
}
