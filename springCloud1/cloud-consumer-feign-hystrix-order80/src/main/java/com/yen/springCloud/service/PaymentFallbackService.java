package com.yen.springCloud.service;

// https://www.youtube.com/watch?v=ZaeVb4_yBdU&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=57

import org.springframework.stereotype.Service;

@Service
public class PaymentFallbackService implements PaymentFeignHystrixService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return ">>> PaymentFallbackService : paymentInfo_OK";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return ">>> PaymentFallbackService : paymentInfo_Timeout";
    }

}
