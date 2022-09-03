package com.wudimanong.payment.service;

import com.wudimanong.payment.entity.bo.UnifiedPayBO;
import com.wudimanong.payment.entity.dto.UnifiedPayDTO;

/**
 * @author jiangqiao
 */
public interface PayChannelService {

    /**
     * 渠道支付接入入口方法
     *
     * @param unifiedPayDTO
     * @return
     */
    UnifiedPayBO pay(UnifiedPayDTO unifiedPayDTO);
}
