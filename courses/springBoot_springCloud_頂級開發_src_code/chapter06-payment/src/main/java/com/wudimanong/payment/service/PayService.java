package com.wudimanong.payment.service;

import com.wudimanong.payment.entity.bo.UnifiedPayBO;
import com.wudimanong.payment.entity.dto.UnifiedPayDTO;

/**
 * @author jiangqiao
 */
public interface PayService {

    /**
     * 统一支付业务层接口方法
     *
     * @param unifiedPayDTO
     * @return
     */
    UnifiedPayBO unifiedPay(UnifiedPayDTO unifiedPayDTO);
}
