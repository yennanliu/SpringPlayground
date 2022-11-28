package com.yen.wallet.dao.model;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/dao/model/UserBalanceFlowPO.java

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

@Data
@TableName("user_balance_flow")
public class UserBalanceFlowPO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 账户变动流水号
     */
    private String flowNo;
    /**
     * 账户号
     */
    private String accNo;

    /**
     * 业务类型
     */
    private String busiType;

    /**
     * 变动金额
     */
    private Integer amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 变动前金额
     */
    private Integer beginBalance;

    /**
     * 变动后金额
     */
    private Integer endBalance;

    /**
     * 借贷方向
     */
    private String fundDirect;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}