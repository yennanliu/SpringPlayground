package com.yen.springCloud.controller;

// https://www.youtube.com/watch?v=f2emw-DPJ_A&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=44

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;
import com.yen.springCloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){

        CommonResult<Payment> res = paymentFeignService.getPaymentById(id);
        log.info(">>> res = " + res);
        return res;
    }

    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        // openfeign-ribbon : client wait 1 sec before timeout
        return paymentFeignService.paymentFeignTimeout();
    }

}
