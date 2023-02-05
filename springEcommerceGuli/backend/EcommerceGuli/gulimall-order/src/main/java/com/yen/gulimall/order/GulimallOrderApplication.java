package com.yen.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yen.gulimall.order.dao")
@SpringBootApplication
public class GulimallOrderApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallOrderApplication.class, args);
	}

}
