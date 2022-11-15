package com.yen.wallet.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/bo/AddBalanceBO.java

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBalanceBO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 增加金额
     */
    private Integer amount;

    /**
     * 业务类型
     */
    private String busiType;

    /**
     * 账户类型
     */
    private String accType;
    /**
     * 币种
     */
    private String currency;
}
