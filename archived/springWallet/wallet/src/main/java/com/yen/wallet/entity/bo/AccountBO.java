package com.yen.wallet.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/bo/AccountBO.java

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class AccountBO implements Serializable{

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
     * 格式化日期显示
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // TODO : double check it
    private Timestamp createTime;

    /**
     * 格式化日期显示
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

}
