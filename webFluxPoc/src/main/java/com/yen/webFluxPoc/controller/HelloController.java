package com.yen.webFluxPoc.controller;

// https://youtu.be/HM7AqhJo1vg?si=Wo9roCLCYtsDHfna&t=207

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    return Flux.just("world !!!!", "lol");
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
