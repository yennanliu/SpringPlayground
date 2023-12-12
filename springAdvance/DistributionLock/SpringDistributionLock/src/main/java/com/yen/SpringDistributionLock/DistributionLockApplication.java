package com.yen.SpringDistributionLock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yen.SpringDistributionLock")
@MapperScan("com.yen.SpringDistributionLock.mapper")
@SpringBootApplication
public class DistributionLockApplication {

	public static void main(String[] args) {

		SpringApplication.run(DistributionLockApplication.class, args);
	}

}
