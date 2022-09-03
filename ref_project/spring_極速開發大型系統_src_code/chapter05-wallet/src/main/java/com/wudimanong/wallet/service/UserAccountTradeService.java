package com.wudimanong.wallet.service;

import com.wudimanong.wallet.entity.bo.AccountChargeBO;
import com.wudimanong.wallet.entity.bo.PayNotifyBO;
import com.wudimanong.wallet.entity.dto.AccountChargeDTO;
import com.wudimanong.wallet.entity.dto.PayNotifyDTO;

/**
 * @author jiangqiao
 */
public interface UserAccountTradeService {

    /**
     * 余额充值业务层接口方法
     *
     * @param accountChargeDTO
     * @return
     */
    AccountChargeBO chargeOrder(AccountChargeDTO accountChargeDTO);

    /**
     * 充值支付结果异步通知接收业务层接口方法
     *
     * @param payNotifyDTO
     * @return
     */
    PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO);
}
