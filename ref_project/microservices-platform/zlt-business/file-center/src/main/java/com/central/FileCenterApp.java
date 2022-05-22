package com.central;

import com.central.common.ribbon.annotation.EnableFeignInterceptor;
import com.central.file.properties.FileServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 文件中心
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@EnableDiscoveryClient
@EnableConfigurationProperties(FileServerProperties.class)
@EnableFeignClients
@EnableFeignInterceptor
@SpringBootApplication
public class FileCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(FileCenterApp.class, args);
    }
}