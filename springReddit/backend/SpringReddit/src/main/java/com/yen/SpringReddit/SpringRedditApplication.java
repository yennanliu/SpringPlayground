package com.yen.SpringReddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
// disable spring security for now
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class SpringRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				SpringRedditApplication.class, args);
	}

}
