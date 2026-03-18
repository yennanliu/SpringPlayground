package com.yen.wallet.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/bo/AccountChargeBO.java

import java.io.Serializable;
import lombok.Data;

@Data
public class AccountChargeBO implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 充值金额，单位为分
     */
    private Integer amount;

    /**
     * 充值币种
     */
    private String currency;

    /**
     * 充值业务订单号（钱包系统生成）
     */
    private String orderId;

    /**
     * 支付系统唯一流水号(调用支付系统后生成)
     */
    private String tradeNo;

    /**
     * 前端唤起支付收银台所需的额外信息
     */
    private String extraInfo;
}