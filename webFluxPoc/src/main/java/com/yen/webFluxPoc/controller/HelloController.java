package com.yen.webFluxPoc.controller;

// https://youtu.be/HM7AqhJo1vg?si=Wo9roCLCYtsDHfna&t=207

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/hello")
public class HelloController {

  @GetMapping("/dev1")
  public String hello() {
    return "hello";
  }

  @GetMapping("/dev2")
  public Mono<String> monoHello() {
    return Mono.just("world !!!!");
  }

  @GetMapping("/dev3")
  public Flux<String> fluxHello() {
    //return Flux.just("world !!!!", "lol");
    return Flux.just(0)
            .map(x -> 10 / x)
            .map(x -> "res = " + x);
  }

  // https://youtu.be/xUux3Ycjh7U?si=aP2uo5VTgKc7ory2&t=396
  @GetMapping("/dev4")
  public Mono<String> hello4(ServerWebExchange serverWebExchange, WebSession webSession, HttpEntity httpEntity) {

    /**
     *   serverWebExchange: can get request, response
     */
    ServerHttpRequest request = serverWebExchange.getRequest();
    ServerHttpResponse response = serverWebExchange.getResponse();

    /** webSession  : can modify session
     */
    Map<String, Object> attrs = webSession.getAttributes();


    /** httpEntity  : get http entity
     */
    httpEntity.getBody();

    return Mono.just("world !!!!");
  }

  /**
   * SSE demo
   *
   * <p>(server send event) 服務端事件推送
   *
   * <p>NOTE !!! need to use "produces = MediaType.TEXT_EVENT_STREAM_VALUE"
   */
  @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> sseDemo() {
    return Flux
            .range(1, 10)
            .map(x -> "hey - " + x)
            .delayElements(Duration.ofMillis(500));
  }

}
