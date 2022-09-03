package com.wudimanong.wallet.entity.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
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
