package com.yen.webFluxPoc.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TestService {

    public Mono<String> delayMsg(){
        return Mono.just("hello! this is delay msg").delayElement(Duration.ofSeconds(5));
    }
}
