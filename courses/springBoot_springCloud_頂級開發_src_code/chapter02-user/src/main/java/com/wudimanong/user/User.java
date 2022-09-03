package com.wudimanong.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author joe
 */
@EnableDiscoveryClient
@SpringBootApplication
public class User {

    public static void main(String[] args) {
        SpringApplication.run(User.class, args);
    }

}
