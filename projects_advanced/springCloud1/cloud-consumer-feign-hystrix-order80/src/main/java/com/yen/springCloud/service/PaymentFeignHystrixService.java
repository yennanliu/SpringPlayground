package com.yen.springCloud.service;

// https://www.youtube.com/watch?v=AO0iGzLF-M0&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=53
// https://www.youtube.com/watch?v=ZaeVb4_yBdU&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=57

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class) // cloud-provider-hystrix-payment // NOTE !!! we have to define which service on eureka we want to call, with feign client
public interface PaymentFeignHystrixService {

    /** method from PaymentController in cloud-provider-payment8001 */
    // encapsulate result
    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id);


    /** method from PaymentController in cloud-provider-payment8001 */
    @GetMapping("/payment/hystrix/timeout/{id}")
    // method for time out demo
    public String paymentInfo_Timeout(@PathVariable("id") Integer id);

}