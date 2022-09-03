package com.wudimanong.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiangqiao
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MonitorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorDemoApplication.class, args);
    }
}
