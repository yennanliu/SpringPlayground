package com.yen.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yen.gulimall.ware.dao")
@SpringBootApplication
public class GulimallWareApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallWareApplication.class, args);
	}

}
