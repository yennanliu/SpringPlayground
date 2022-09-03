package com.wudimanong.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiangqiao
 */
@EnableDiscoveryClient
@SpringBootApplication
public class DevopsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevopsDemoApplication.class, args);
    }
}
