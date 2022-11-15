package com.yen.wallet.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/UserAccountTradeService.java

import com.yen.wallet.entity.bo.AccountChargeBO;
import com.yen.wallet.entity.bo.PayNotifyBO;
import com.yen.wallet.entity.dto.AccountChargeDTO;
import com.yen.wallet.entity.dto.PayNotifyDTO;

public interface UserAccountTradeService {

    /** top up account */
    AccountChargeBO chargeOrder(AccountChargeDTO accountChargeDTO);

    /** async receive top up outcome notification */
    PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO);
}
