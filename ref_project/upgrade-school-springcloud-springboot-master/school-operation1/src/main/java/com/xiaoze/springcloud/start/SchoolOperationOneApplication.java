package com.xiaoze.springcloud.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * MainOperationApplication
 *
 * @author xiaoze
 * @date 2018/6/7
 *
 * EnableEurekaClient 服务发现，会去注册中心自动注册服务，Eureka专用
 * EnableDiscoveryClient 服务发现，会去注册中心自动注册服务
 * ComponentScan(basePackages = "edu.fjnu")  这里指定你要扫描的包及其子包子类
 * MapperScan("com.xiaoze.springcloud.mapper") 扫描：该包下相应的class,主要是MyBatis的持久化类.
 *
 */

@EnableEurekaClient
@ComponentScan(basePackages = "com.xiaoze.springcloud")
@SpringBootApplication
@MapperScan("com.xiaoze.springcloud.mapper")
@EnableTransactionManagement
public class SchoolOperationOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolOperationOneApplication.class, args);
    }
}
