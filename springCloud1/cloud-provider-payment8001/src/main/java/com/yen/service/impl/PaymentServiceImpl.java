package com.yen.service.impl;

import com.yen.bean.Payment;
import com.yen.dao.PaymentDao;
import com.yen.service.PaymentService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource  // use @Resource rather than @Autowired
    PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }

}
