package com.xiaoze.springcloud.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * MainSchoolApplication
 *
 * @author xiaoze
 * @date 2018/6/7
 *
 * EnableEurekaClient 服务发现，会去注册中心自动注册服务，Eureka专用
 * EnableDiscoveryClient 服务发现，会去注册中心自动注册服务
 * ComponentScan(basePackages = "edu.fjnu")  这里指定你要扫描的包及其子包子类
 * EnableFeignClients 扫描Feign的包及其子包子类
 * EnableCircuitBreaker对hystrixR熔断机制的支持
 *
 */
@EnableFeignClients(basePackages= {"com.xiaoze.springcloud"})
@ComponentScan(basePackages = "com.xiaoze.springcloud")
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class MainOperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainOperationApplication.class, args);
    }
}
