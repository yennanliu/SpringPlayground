package com.yen.springMVC1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class SpringMvc1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringMvc1Application.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return String.format("Hello %s!");
	}
}
