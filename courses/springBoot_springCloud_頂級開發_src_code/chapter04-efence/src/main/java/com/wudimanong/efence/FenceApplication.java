package com.wudimanong.efence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author jiangqiao
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.wudimanong.efence.dao.mapper")
public class FenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FenceApplication.class, args);
    }
}
