package com.yen.controller;

// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/** Payment consumer ( consumer-order-80)
 *
 *  -> will call cloud-provider-payment8001 services via httpClient (via RestTemplate)
 */

@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderController {

    public static final String PAYMENT_URL = "http://localhost:8001";

    @Resource
    RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommonResult<Payment> create(Payment payment){
        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
         *
         *  -> postForObject for calling POST request
         */
        log.info(">>> consumer-order-80 call /payment/create ...", payment);
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
         *
         *  -> getForObject for calling GET request
         */
        log.info(">>> consumer-order-80 call /payment/get/{id} ...", id);
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

}
