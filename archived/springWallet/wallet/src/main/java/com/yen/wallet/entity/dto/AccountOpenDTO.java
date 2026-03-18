package com.yen.wallet.entity.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/dto/AccountOpenDTO.java

import com.yen.wallet.validator.EnumValue;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

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

    //@EnumValue(strValues = {"JPY", "USD"})
    private String currency;
}