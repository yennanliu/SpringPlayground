package com.yen.springPayment.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/PayService.java

import entity.bo.UnifiedPayBO;
import entity.dto.UnifiedPayDTO;

public interface PayService {

    /** unified pay method */
    UnifiedPayBO unifiedPay(UnifiedPayDTO unifiedPayDTO);
}
