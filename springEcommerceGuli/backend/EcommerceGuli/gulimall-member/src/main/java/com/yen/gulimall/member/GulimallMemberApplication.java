package com.yen.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// https://youtu.be/wIR4X0mYSa0?t=350

/**
 *  To call other services via Feign client call:
 *
 *		- https://youtu.be/G1SNCTRcKdE?t=227
 *
 *		- 1) install open-feign
 *		- 2) create an interface tells springCloud that this service needs feign call
 *			- under feign pkg
 *			- declare which service, which endpoint the interface method is calling to
 *		- 3) enable feign remote call (EnableFeignClients)
 */
@EnableFeignClients(basePackages = "com.yen.gulimall.member.feign")
@EnableDiscoveryClient
@MapperScan("com.yen.gulimall.member.dao")
@SpringBootApplication
public class GulimallMemberApplication {

	public static void main(String[] args) {

		SpringApplication.run(GulimallMemberApplication.class, args);
	}

}
