package com.wudimanong.wallet.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("user_balance_order")
public class UserBalanceOrderPO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 余额充值订单ID
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 充值订单金额
     */
    private Integer amount;

    /**
     * 订单交易类型，charge-余额充值；refund-余额退款
     */
    private String tradeType;

    /**
     * 币种
     */
    private String currency;

    /**
     * 支付流水号
     */
    private String tradeNo;

    /**
     * 支付状态（0-待支付；1-支付中；2-支付成功；3-支付失败）
     */
    private String status;

    /**
     * 是否自动续费充值，0-不自动续费；1-自动续费
     */
    private Integer isRenew;

    /**
     * 交易发生时间
     */
    private Timestamp tradeTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}
