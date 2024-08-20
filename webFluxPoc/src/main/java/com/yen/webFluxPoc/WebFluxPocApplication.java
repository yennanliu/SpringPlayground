package com.yen.webFluxPoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

// https://youtu.be/K1etTHjgJSQ?si=rwDnmu4gNeFJYX_3&t=143

@EnableWebFlux // all default setting by @WebFluxAutoConfiguration will be disabled : https://youtu.be/xUux3Ycjh7U?si=YCYzEiMo7vQTiotv&t=2068
@SpringBootApplication
public class WebFluxPocApplication {

  public static void main(String[] args) {

    System.out.println("Web flux app start");
    SpringApplication.run(WebFluxPocApplication.class, args);

    /** run webflux server from scratch
     *
     *  https://youtu.be/K1etTHjgJSQ?si=pKNDRWrklvcVAyI7&t=306
     */
    // TODO : fix below
//    HttpHandler httpHandler = (ServerHttpRequest request, ServerHttpResponse response) -> {
//      return Mono.empty();
//    };
//
//    ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
//
//    // start Netty server
//    HttpServer
//            .create()
//            .host("localhost")
//            .port(7777)
//            .handle(adapter)
//            .bindNow();

  }

}
