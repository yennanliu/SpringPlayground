package com.yen.springcloud.controller;

// https://www.youtube.com/watch?v=BFYHIlX_Sts&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=51

import com.yen.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    // get server port from application.yml
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){

        String result = paymentService.paymentInfo_OK(id);
        log.info(">>> result = {}", result);
        return result;
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    public String paymentInfo_TIMEOUT(@PathVariable("id") Integer id){

        String result = paymentService.paymentInfo_Timeout(id);
        log.info(">>> result = {}", result);
        return result;
    }

}
