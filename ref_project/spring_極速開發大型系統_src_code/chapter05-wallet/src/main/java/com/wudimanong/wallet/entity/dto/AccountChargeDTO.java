package com.wudimanong.wallet.entity.dto;

import com.wudimanong.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class AccountChargeDTO implements Serializable {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 充值金额以分为单位
     */
    private Integer amount;

    /**
     * 充值币种(仅支持人民币)
     */
    @EnumValue(strValues = {"CNY"})
    private String currency;

    /**
     * 支付类型（0-微信支付；1-支付宝支付）
     */
    @EnumValue(intValues = {0, 1})
    private Integer paymentType;

    /**
     * 是否自动续费
     */
    @EnumValue(intValues = {0, 1})
    private Integer isRenew;
}
