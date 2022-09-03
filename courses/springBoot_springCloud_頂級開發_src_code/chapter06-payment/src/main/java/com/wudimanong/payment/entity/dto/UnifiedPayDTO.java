package com.wudimanong.payment.entity.dto;

import com.wudimanong.payment.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class UnifiedPayDTO implements Serializable {

    /**
     * 接入方应用ID
     */
    @NotNull(message = "应用ID不能为空")
    private String appId;

    /**
     * 接入方支付订单ID，必须在接入方系统唯一（如钱包系统）
     */
    @NotNull(message = "支付订单ID不能为空")
    private String orderId;

    /**
     * 交易类型，用于标示具体的业务类型，如topup-钱包充值等，可以根据具体业务定义
     */
    @EnumValue(strValues = {"topup"})
    private String tradeType;

    /**
     * 支付渠道，例如0-微信支付，1-支付宝支付
     */
    @EnumValue(intValues = {0, 1})
    private Integer channel;

    /**
     * 支付产品定义，用于区分具体的渠道支付产品，例如:ALI_PAY_H5-表示支付宝H5支付，具体可根据实际情况定义
     */
    private String payType;

    /**
     * 支付金额，以分为单位,数值必须大于0
     */
    private Integer amount;

    /**
     * 支付币种，默认为CNY
     */
    @EnumValue(strValues = {"CNY"})
    private String currency;

    /**
     * 商户系统唯一标示用户身份的ID
     */
    @NotNull(message = "用户ID不能为空")
    private String userId;

    /**
     * 商品标题，一般支付渠道对此会有要求
     */
    @NotNull(message = "商品标题不能为空")
    private String subject;

    /**
     * 商品描述信息
     */
    private String body;

    /**
     * 支付扩展信息，如针对微信、支付宝等有特定渠道参数的进行补充
     */
    private Object extraInfo;

    /**
     * 异步支付结果通知地址
     */
    @NotNull(message = "支付通知地址不能为空")
    private String notifyUrl;

    /**
     * 同步支付结果跳转地址（支付成功后同步跳转回商户界面的URL）
     */
    private String returnUrl;
}
