package com.yen.webFluxPoc.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

// https://youtu.be/II52GMXir4E?si=j7yBZUBDezlPoNcc

@Service
public class TestService {

  public Mono<String> delayMsg() {
    return Mono.just("hello! this is delay msg").delayElement(Duration.ofSeconds(5));
  }

  public Mono<String> getFromDB() {
    return Mono.just("DB msg").delayElement(Duration.ofSeconds(5));
  }
}
