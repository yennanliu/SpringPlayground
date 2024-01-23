package com.yen.springBankApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 // Fix using Swagger 2.x : https://blog.51cto.com/u_15740726/5540690
@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankApplication.class, args);
	}

}