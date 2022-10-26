package com.xiaoze.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * ConfigServerApplication
 *
 * @author xiaoze
 * @date 2018/6/7
 *
 * EnableEurekaClient 服务发现，会去注册中心自动注册服务，Eureka专用
 */

@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
