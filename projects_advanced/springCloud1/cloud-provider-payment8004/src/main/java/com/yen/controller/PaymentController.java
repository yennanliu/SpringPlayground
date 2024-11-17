package com.yen.controller;

// https://www.youtube.com/watch?v=eKnWj_rDQO0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=29

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/payment/zk")
    public String paymentZK(){

        return "spring cloud with ZK : " + serverPort + UUID.randomUUID().toString();
    }

}
