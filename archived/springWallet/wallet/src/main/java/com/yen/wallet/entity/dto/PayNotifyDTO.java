package com.yen.wallet.entity.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/dto/PayNotifyDTO.java

import com.yen.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

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