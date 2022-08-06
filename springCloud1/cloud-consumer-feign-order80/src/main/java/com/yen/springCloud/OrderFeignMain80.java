package com.yen.springCloud;

//https://www.youtube.com/watch?v=f2emw-DPJ_A&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=44

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // NOTE !!! we HAVE to enable Feign client, so spring boot can use it
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(
                OrderFeignMain80.class, args
        );
    }

}
