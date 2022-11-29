package com.yen.wallet.service.impl;

// book p. 5-18
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserAccountTradeServiceImpl.java

import com.yen.wallet.client.PaymentClient;
import com.yen.wallet.dao.mapper.UserBalanceOrderDao;
import com.yen.wallet.dao.model.UserBalanceOrderPO;
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
        // generate charge order info
        UserBalanceOrderPO userBalanceOrderPO = create
        try{
            userBalanceOrderDao.insert(u)
        }catch (Exception e){

        }

        return null;
    }

    @Override
    public PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO) {
        return null;
    }

    /** helper methods */
    private UserBalanceOrderPO createChargeOrder(AccountChargeDTO accountChargeDTO) {
        UserBalanceOrderPO userBalanceOrderPO = UserBalanceOrderConvert.INSTANCE
                .convertUserBalanceOrderPO(accountChargeDTO);
        //生成充值订单流水号
        String orderId = getOrderId();
        userBalanceOrderPO.setOrderId(orderId);
        //设置交易类型为充值
        userBalanceOrderPO.setTradeType(TradeType.CHARGE.getCode());
        //设置支付状态为待支付
        userBalanceOrderPO.setStatus("0");
        //设置交易时间
        userBalanceOrderPO.setTradeTime(new Timestamp(System.currentTimeMillis()));
        //设置订单创建时间
        userBalanceOrderPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //设置订单初始更新时间
        userBalanceOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userBalanceOrderPO;
    }

}
