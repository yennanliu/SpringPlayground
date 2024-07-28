package com.yen.webFluxPoc.controller;

import com.yen.webFluxPoc.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

// https://youtu.be/II52GMXir4E?si=QX0ifDZx32PKxtKc

@RestController
@RequestMapping(value="/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String hello(){
        return "hello!!";
    }

    @GetMapping("/delay")
    public Mono<String> getDelayMsg(){
        return testService.delayMsg();
    }

    /**
     *  NOTE !!!
     *
     *   via zipWith, we can return the final result when both delayMsg and getFromDB response are received
     */
    @GetMapping("/delay_zip")
    public Mono<String> getZipMsg(){
        return testService.delayMsg().zipWith(testService.getFromDB())
                .map(value -> {
                    return value.getT1() + value.getT2();
                });
    }

}
