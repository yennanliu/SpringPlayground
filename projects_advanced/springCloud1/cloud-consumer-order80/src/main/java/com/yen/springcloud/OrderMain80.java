package com.yen.springcloud;

// https://www.youtube.com/watch?v=ipbTc7vYtcQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=39

import com.yen.myrule.MyselfRule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-ORDER-SERVICE", configuration = MyselfRule.class) // Not use default round-robbin, but use our own defined rules (configuration = MyselfRule.class)
public class OrderMain80 {
    public static void main(String[] args) {

        SpringApplication.run(OrderMain80.class, args);
    }

}