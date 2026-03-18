package com.yen.wallet.controller;

// book p.5-21
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/controller/UserAccountTradeController.java

import com.yen.wallet.entity.ResponseResult;
import com.yen.wallet.entity.bo.AccountChargeBO;
import com.yen.wallet.entity.bo.PayNotifyBO;
import com.yen.wallet.entity.dto.AccountChargeDTO;
import com.yen.wallet.entity.dto.PayNotifyDTO;
import com.yen.wallet.service.UserAccountTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class UserAccountTradeController {

    @Autowired
    UserAccountTradeService userAccountTradeService;

    /** account top up endpoint */
    @PostMapping("/chargeOrder")
    public ResponseResult<AccountChargeBO> chargeOrder(@RequestBody @Validated AccountChargeDTO accountChargeDTO) {

        return ResponseResult.OK(userAccountTradeService.chargeOrder(accountChargeDTO));
    }

    /** payment result notification endpoint */
    @PostMapping("/payNotify")
    public ResponseResult<PayNotifyBO> receivePayNotify(@RequestBody @Validated PayNotifyDTO payNotifyDTO) {

        return ResponseResult.OK(userAccountTradeService.receivePayNotify(payNotifyDTO));
    }

}
