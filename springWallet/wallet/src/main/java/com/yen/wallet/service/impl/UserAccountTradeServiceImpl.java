package com.yen.wallet.service.impl;

// book p. 5-18
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserAccountTradeServiceImpl.java

import com.yen.wallet.client.PaymentClient;
import com.yen.wallet.dao.mapper.UserBalanceOrderDao;
import com.yen.wallet.entity.bo.AccountChargeBO;
import com.yen.wallet.entity.bo.PayNotifyBO;
import com.yen.wallet.entity.dto.AccountChargeDTO;
import com.yen.wallet.entity.dto.PayNotifyDTO;
import com.yen.wallet.service.UserAccountTradeService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAccountTradeServiceImpl implements UserAccountTradeService {

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    UserBalanceOrderDao userBalanceOrderDao;

    @Override
    public AccountChargeBO chargeOrder(AccountChargeDTO accountChargeDTO) {
        return null;
    }

    @Override
    public PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO) {
        return null;
    }

}
