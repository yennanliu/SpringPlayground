package com.yen.springCloud.service;

// https://www.youtube.com/watch?v=f2emw-DPJ_A&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=44

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE") // NOTE !!! we have to define which service on eureka we want to call, with feign client
public interface PaymentFeignService {

    /** method from PaymentController in cloud-provider-payment8001 */
    // encapsulate result
    @GetMapping("/payment/get/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

}
