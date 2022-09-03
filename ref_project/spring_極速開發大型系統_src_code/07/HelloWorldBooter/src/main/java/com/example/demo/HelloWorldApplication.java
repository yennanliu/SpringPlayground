package com.example.demo;

import javafx.application.Application;
import javafx.scene.Parent;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication

/*用下面三個註釋替代註釋@SpringBootApplication*/
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class HelloWorldApplication {

  public static void main(String[] args) {

		SpringApplication.run(HelloWorldApplication.class, args);
	}

/* public static void main(String[] args) {
		SpringApplication springApplication= new SpringApplication(HelloWorldApplication.class);
		//springApplication.run();
       //springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.run(args);
	}*/

/*建立多階層的“ApplicationContext*/
/*	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(Parent.class)
				.child(HelloWorldApplication.class)
				.run(args);
}*/

}
