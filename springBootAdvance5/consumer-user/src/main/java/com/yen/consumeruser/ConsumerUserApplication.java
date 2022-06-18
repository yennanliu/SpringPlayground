package com.yen.consumeruser;

// https://www.youtube.com/watch?v=sXc0bmtJ-cw&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=37

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient // enable service discovery
@SpringBootApplication
public class ConsumerUserApplication {

	public static void main(String[] args) {

		SpringApplication.run(ConsumerUserApplication.class, args);
	}

	// TODO : check how does this work !!
	/** via RestTemplate, we can send HTTP request */
	@LoadBalanced // enable load balance mechanism
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
