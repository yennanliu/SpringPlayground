package com.yen.webFluxPoc.webFluxPoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class WebFluxPocApplication {

	public static void main(String[] args) {

		System.out.println("Web flux app start");
		SpringApplication.run(
				WebFluxPocApplication.class, args);
	}

}
