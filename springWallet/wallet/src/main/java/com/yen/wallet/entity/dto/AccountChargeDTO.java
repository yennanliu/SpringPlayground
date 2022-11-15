package com.yen.wallet.entity.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/dto/AccountChargeDTO.java

import com.yen.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountChargeDTO implements Serializable{

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 充值金额以分为单位
     */
    private Integer amount;

    //@EnumValue(strValues = {"JPY"})
    private String currency;

    /**
     * 支付类型（0-微信支付；1-支付宝支付）
     */
    @EnumValue(intValues = {0, 1}) // TODO : double check it (EnumValue)
    private Integer paymentType;

    /**
     * 是否自动续费
     */
    @EnumValue(intValues = {0, 1})
    private Integer isRenew;

}
