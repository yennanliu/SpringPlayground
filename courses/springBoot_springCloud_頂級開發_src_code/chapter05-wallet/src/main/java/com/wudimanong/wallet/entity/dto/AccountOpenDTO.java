package com.wudimanong.wallet.entity.dto;

import com.wudimanong.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class AccountOpenDTO implements Serializable {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 账户类型，0-现金账户；1-赠送金账户
     */
    @EnumValue(intValues = {0, 1}, message = "账户类型输入有误")
    private Integer accType;

    /**
     * 账户币种(仅支持人民币、美元两种账户)
     */
    @EnumValue(strValues = {"CNY", "USD"})
    private String currency;
}
