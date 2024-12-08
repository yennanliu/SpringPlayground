package com.yen.RedisCache1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisCache1Application {

	public static void main(String[] args) {

		SpringApplication.run(RedisCache1Application.class, args);
	}

}
