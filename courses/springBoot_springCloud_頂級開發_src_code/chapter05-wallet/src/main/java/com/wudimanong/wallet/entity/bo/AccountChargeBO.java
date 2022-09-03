package com.wudimanong.wallet.entity.bo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author jiangqiao
 */
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
