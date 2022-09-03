package com.wudimanong.payment.controller;

import com.wudimanong.payment.entity.dto.AliPayReceiveDTO;
import com.wudimanong.payment.service.PayNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/notify")
public class PayNotifyController {

    @Autowired
    PayNotifyService payNotifyServiceImpl;

    /**
     * 支付宝异步支付结果通知接收接口定义
     */
    @PostMapping("/aliPayReceive")
    public String aliPayReceive(AliPayReceiveDTO aliPayReceiveDTO) {
        return payNotifyServiceImpl.aliPayReceive(aliPayReceiveDTO);
    }
}
