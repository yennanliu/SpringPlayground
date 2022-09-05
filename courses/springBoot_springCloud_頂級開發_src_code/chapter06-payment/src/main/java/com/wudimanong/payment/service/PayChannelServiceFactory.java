package com.wudimanong.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class PayChannelServiceFactory {

    @Autowired
    private PayChannelService aliPayServiceImpl;

    /**
     * 根据渠道代码获取具体的渠道Service层处理类
     *
     * @param channelName
     * @return
     */
    public PayChannelService createPayChannelService(int channelName) {
        switch (channelName) {
            case 1:
                return aliPayServiceImpl;
            default:
                return null;
        }
    }
}
