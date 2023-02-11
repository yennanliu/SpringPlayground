package com.yen.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *  https://youtu.be/F8WyHeaEf9E?t=121
 *
 *  	1) enable service discovery
 *  	2) register to Nacos
 *
 *
 */

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // remove DB relative auto config, since geteway service is NOT using DB : https://youtu.be/F8WyHeaEf9E?t=408
public class GulimallGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallGatewayApplication.class, args);
	}

}
