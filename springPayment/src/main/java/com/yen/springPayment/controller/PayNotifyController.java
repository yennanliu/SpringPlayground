package com.yen.springPayment.controller;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/controller/PayNotifyController.java

import com.yen.springPayment.service.PayNotifyService;
import entity.dto.AliPayReceiveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notify")
public class PayNotifyController {

    @Autowired
    PayNotifyService payNotifyService;

    /** endpoint notify users when get async Ali pay result */
    @PostMapping("/aliPayReceive")
    public String aliPayReceive(AliPayReceiveDTO aliPayReceiveDTO) {
        return payNotifyService.aliPayReceive(aliPayReceiveDTO);
    }

}
