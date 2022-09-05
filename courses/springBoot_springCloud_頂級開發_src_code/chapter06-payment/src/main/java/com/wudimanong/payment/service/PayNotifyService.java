package com.wudimanong.payment.service;

import com.wudimanong.payment.entity.dto.AliPayReceiveDTO;

/**
 * @author jiangqiao
 */
public interface PayNotifyService {

    /**
     * 支付宝异步支付结果通知
     *
     * @param aliPayReceiveDTO
     * @return
     */
    String aliPayReceive(AliPayReceiveDTO aliPayReceiveDTO);

}
