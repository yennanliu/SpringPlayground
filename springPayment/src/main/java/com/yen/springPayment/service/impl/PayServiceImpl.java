package com.yen.springPayment.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/impl/PayServiceImpl.java

import com.yen.springPayment.dao.mapper.PayOrderDao;
import com.yen.springPayment.service.PayService;
import entity.bo.UnifiedPayBO;
import entity.dto.UnifiedPayDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    /** redis distributed lock prefix */
    public final String redisLockPrefix = "pay-order&";

    /** redis distributed lock dep */
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Autowired
    PayOrderDao payOrderDao;

    @Override
    public UnifiedPayBO unifiedPay(UnifiedPayDTO unifiedPayDTO) {
        return null;
    }

}
