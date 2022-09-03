package com.wudimanong.wallet.controller;

import com.wudimanong.wallet.entity.ResponseResult;
import com.wudimanong.wallet.entity.bo.AccountChargeBO;
import com.wudimanong.wallet.entity.bo.PayNotifyBO;
import com.wudimanong.wallet.entity.dto.AccountChargeDTO;
import com.wudimanong.wallet.entity.dto.PayNotifyDTO;
import com.wudimanong.wallet.service.UserAccountTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class UserAccountTradeController {

    /**
     * 依赖注入Service层接口
     */
    @Autowired
    UserAccountTradeService userAccountTradeServiceImpl;

    /**
     * 创建余额充值订单接口
     *
     * @param accountChargeDTO
     * @return
     */
    @PostMapping("/chargeOrder")
    public ResponseResult<AccountChargeBO> chargeOrder(@RequestBody @Validated AccountChargeDTO accountChargeDTO) {
        return ResponseResult.OK(userAccountTradeServiceImpl.chargeOrder(accountChargeDTO));
    }

    /**
     * 支付结果通知接收接口
     *
     * @param payNotifyDTO
     * @return
     */
    @PostMapping("/payNotify")
    public ResponseResult<PayNotifyBO> receivePayNotify(@RequestBody @Validated PayNotifyDTO payNotifyDTO) {
        return ResponseResult.OK(userAccountTradeServiceImpl.receivePayNotify(payNotifyDTO));
    }
}
