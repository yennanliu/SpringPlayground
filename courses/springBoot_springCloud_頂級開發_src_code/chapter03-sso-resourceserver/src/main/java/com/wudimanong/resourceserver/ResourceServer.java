package com.wudimanong.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiangqiao
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ResourceServer {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServer.class, args);
    }
}
