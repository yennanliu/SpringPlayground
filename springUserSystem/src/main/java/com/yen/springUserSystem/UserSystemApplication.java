package com.yen.springUserSystem;

// book p.2-36

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserSystemApplication.class, args);
	}

}
