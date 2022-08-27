package com.yen.springCloud.controller;

// https://www.youtube.com/watch?v=NGhYY67j1kc&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=56

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

    @HystrixCommand(
            fallbackMethod = "paymentTimeOutFallbackMethod",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "1500")}) // backup method when timeout happen
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id){
        String result = paymentFeignService.paymentInfo_Timeout(id);
        return result;
    }

    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id){
        return ">>> consumer 80, payment system NO response after 10 sec, plz check/retry later ...";
    }

}
