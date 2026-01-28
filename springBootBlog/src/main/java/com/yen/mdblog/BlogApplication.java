package com.yen.mdblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
// all default setting by @WebFluxAutoConfiguration will be disabled : https://youtu.be/xUux3Ycjh7U?si=YCYzEiMo7vQTiotv&t=2068
@SpringBootApplication
public class BlogApplication {
  public static void main(String[] args) {

    SpringApplication.run(BlogApplication.class, args);
  }
}
