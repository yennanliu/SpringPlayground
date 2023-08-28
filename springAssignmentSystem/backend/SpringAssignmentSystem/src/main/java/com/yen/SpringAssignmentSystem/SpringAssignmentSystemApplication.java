package com.yen.SpringAssignmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.yen.SpringAssignmentSystem.controller.AuthController")
public class SpringAssignmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				SpringAssignmentSystemApplication.class, args);
	}

}
