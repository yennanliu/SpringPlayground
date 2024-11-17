package com.yen.SpringBootPart1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *   Default main application (from spring.io)
 *
 *    -> if only wanna run application (regardless of special op in MainApplication)
 *    -> run this app instead
 */
@SpringBootApplication
public class SpringBootApp {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootApp.class, args);
	}

}
