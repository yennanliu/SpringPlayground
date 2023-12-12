package com.yen.SpringDistributionLock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@org.mybatis.spring.annotation.MapperScan("com.yen.SpringDistributionLock.mapper.StockMapper")
@SpringBootApplication
public class SpringDistributionLockApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringDistributionLockApplication.class, args);
	}

}
