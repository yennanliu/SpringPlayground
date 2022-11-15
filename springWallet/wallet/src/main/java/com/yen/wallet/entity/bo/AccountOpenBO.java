package com.yen.wallet.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/bo/AccountOpenBO.java

import lombok.Data;

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