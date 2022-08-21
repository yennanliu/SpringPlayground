package com.yen.springCloud.controller;

import com.yen.bean.CommonResult;
import com.yen.bean.Payment;
import com.yen.springCloud.service.PaymentFeignHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderFeignHystrixController {

    @Resource
    private PaymentFeignHystrixService paymentFeignService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){

        String result = paymentFeignService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id){
        String result = paymentFeignService.paymentInfo_Timeout(id);
        return result;
    }

}
