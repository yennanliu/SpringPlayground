package com.yen.resourceServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yen.resourceServer")
@SpringBootApplication
@EnableDiscoveryClient
public class ResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

}
