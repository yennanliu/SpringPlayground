package com.yen.service;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10

import com.yen.entities.Payment;

public interface PaymentService {

    public int create(Payment payment);
    public Payment getPaymentById(Long id);
}
