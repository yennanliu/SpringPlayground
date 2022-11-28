package com.yen.wallet.dao.model;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/dao/model/UserBalancePO.java

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

@Data
@TableName("user_balance")
public class UserBalancePO {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 账号编码
     */
    private String accNo;

    /**
     * 账号类型
     */
    private String accType;

    /**
     * 币种
     */
    private String currency;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}