package com.yen.springPayment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.yen.springPayment.dao.mapper")
public class SpringPaymentApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringPaymentApplication.class, args);
	}

}
