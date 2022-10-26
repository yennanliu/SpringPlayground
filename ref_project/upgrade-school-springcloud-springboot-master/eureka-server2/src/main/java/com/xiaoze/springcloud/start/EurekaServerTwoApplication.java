package com.xiaoze.springcloud.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaServerApplication
 *
 * @author xiaoze
 * @date 2018/6/7
 *
 * EnableEurekaServer表示：EurekaServer服务器端启动类,接受其它微服务注册进来
 *
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerTwoApplication.class, args);
    }
}
