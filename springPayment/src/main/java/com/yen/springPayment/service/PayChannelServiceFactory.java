package com.yen.springPayment.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/PayChannelServiceFactory.java

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayChannelServiceFactory {

    @Autowired
    public PayChannelService payChannelService;

    /** method : get channel service based on channel code */
    public PayChannelService createPayChannelService(int channelName) {
        switch (channelName) {
            case 1:
                return payChannelService;
            default:
                return null;
        }
    }

}
