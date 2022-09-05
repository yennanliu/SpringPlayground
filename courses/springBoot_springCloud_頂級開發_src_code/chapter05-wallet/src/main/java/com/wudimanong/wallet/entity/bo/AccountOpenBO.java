package com.wudimanong.wallet.entity.bo;

import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class AccountOpenBO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 钱包系统生成唯一账户号
     */
    private String accNo;

    /**
     * 账户类型
     */
    private String accType;
    /**
     * 币种
     */
    private String currency;
}
