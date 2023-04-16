package com.yen.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan("com.yen.gulimall.ware.dao")
@SpringBootApplication
public class GulimallWareApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallWareApplication.class, args);
	}

}
