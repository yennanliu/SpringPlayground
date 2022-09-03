package com.wudimanong.wallet.entity.dto;

import com.wudimanong.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class PayNotifyDTO implements Serializable {

    /**
     * 商户支付订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderId;

    /**
     * 支付订单金额
     */
    private Integer amount;

    /**
     * 支付币种
     */
    private String currency;

    /**
     * 支付订单状态，0-待支付；1-支付中；2-支付成功；3-支付失败
     */
    @EnumValue(intValues = {2, 3}, message = "只接收支付状态为成功/失败的通知")
    private Integer payStatus;
}
