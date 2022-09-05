package com.wudimanong.payment.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("pay_order")
public class PayOrderPO {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 业务方订单号（需保证业务方系统内唯一）
     */
    private String orderId;

    /**
     * 业务方交易类型
     */
    private String tradeType;

    /**
     * 支付订单金额
     */
    private Integer amount;

    /**
     * 支付币种
     */
    private String currency;

    /**
     * 支付订单状态（0-待支付；1-支付中；2-支付成功；3-支付失败'）
     */
    private String status;

    /**
     * 支付渠道编码
     */
    private String channel;

    /**
     * 渠道支付方式
     */
    private String payType;

    /**
     * 支付系统订单流水号
     */
    private String payId;

    /**
     * 第三方渠道流水号
     */
    private String tradeNo;

    /**
     * 业务方用户ID
     */
    private String userId;

    /**
     * 支付订单创建时间
     */
    private Timestamp createTime;

    /**
     * 支付订单更新时间
     */
    private Timestamp updateTime;

}
