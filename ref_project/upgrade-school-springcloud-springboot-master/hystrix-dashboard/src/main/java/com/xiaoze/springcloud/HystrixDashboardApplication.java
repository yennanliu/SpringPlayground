package com.xiaoze.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * HystrixDashboardApplication
 *
 * @author xiaoze
 * @date 2018/7/6
 *
 * EnableEurekaServer表示：EurekaServer服务器端启动类,接受其它微服务注册进来
 *
 */

@EnableHystrixDashboard
@SpringBootApplication
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
