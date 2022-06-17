package com.yen.eurekaserver;

// https://www.youtube.com/watch?v=P5o-6Od5cfc&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=34

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *  Registry center
 *
 *   1) config Eureka
 *   2) @EnableEurekaServer (Enable Eureka Server)
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
