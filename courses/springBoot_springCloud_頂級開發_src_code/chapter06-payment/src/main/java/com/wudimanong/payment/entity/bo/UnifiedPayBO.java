package com.wudimanong.payment.entity.bo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedPayBO implements Serializable {

    /**
     * 商户支付订单号
     */
    private String orderId;

    /**
     * 第三方支付渠道生成预支付订单号
     */
    private String tradeNo;

    /**
     * 支付订单金额
     */
    private Integer amount;

    /**
     * 支付币种
     */
    private String currency;

    /**
     * 支付渠道编码
     */
    private String channel;

    /**
     * 特殊支付场景所需传递的额外支付信息
     */
    private String extraInfo;

    /**
     * 支付订单状态，0-待支付；1-支付中；2-支付成功；3-支付失败
     */
    private Integer payStatus;
}
