package com.yen.controller;

// https://www.youtube.com/watch?v=WeXWCwD4oX4&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=30

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/** Payment consumer ( consumer-order-80)
 *
 *  -> will call cloud-provider-payment8001 services via httpClient (via RestTemplate)
 */

@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderZKController {

    // single node mode
    //public static final String PAYMENT_URL = "http://localhost:8001";
    // cluster mode (NOTE !!! we ONLY care service name; instead of actual url) (with service name http://CLOUD-PAYMENT-SERVICE, consumer-80 will use either payment8001 or payment8002)
    public static final String INVOKE_URL = "http://cloud-provider-payment";

    @Resource
    RestTemplate restTemplate;

    @GetMapping("/consumer/payment/zk")
    public String paymentInfo(){
        String result = restTemplate.getForObject(INVOKE_URL + "/payment/zk", String.class);
        return result;
    }

//    @GetMapping("/payment/create")
//    public CommonResult<Payment> create(Payment payment){
//        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
//         *
//         *  -> postForObject for calling POST request
//         */
//        log.info(">>> consumer-order-80 call /payment/create ...", payment);
//        return restTemplate.postForObject(INVOKE_URL + "/payment/create", payment, CommonResult.class);
//    }
//
//    @GetMapping("/payment/get/{id}")
//    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
//        /** NOTE !!! call cloud-provider-payment8001 services via httpClient
//         *
//         *  -> getForObject for calling GET request
//         */
//        log.info(">>> consumer-order-80 call /payment/get/{id} ...", id);
//        return restTemplate.getForObject(INVOKE_URL + "/payment/get/" + id, CommonResult.class);
//    }

}
