package com.yen.springCloud.service;

// https://www.youtube.com/watch?v=f2emw-DPJ_A&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=44
// https://www.youtube.com/watch?v=6o4pd_B62SE&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=45

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


    /** method from PaymentController in cloud-provider-payment8001 */
    @GetMapping("/payment/feign/timeout")
    // method for time out demo
    public String paymentFeignTimeout();

}
