package com.yen.SpringReddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication
// disable spring security for now
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableAsync // https://youtu.be/PMr2Mh9xzm4?t=305
public class SpringRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				SpringRedditApplication.class, args);
	}

}
