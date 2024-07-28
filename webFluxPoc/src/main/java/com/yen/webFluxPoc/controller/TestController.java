package com.yen.webFluxPoc.controller;

import com.yen.webFluxPoc.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/delay")
    public Mono<String> getDelayMsg(){
        return testService.delayMsg();
    }

}
