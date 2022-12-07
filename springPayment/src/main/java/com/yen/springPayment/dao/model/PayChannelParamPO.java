package com.yen.springPayment.dao.model;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/dao/model/PayChannelParamPO.java

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Time;
import java.sql.Timestamp;
import lombok.Data;

@Data
@TableName("pay_channel_param")
public class PayChannelParamPO {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 具体支付渠道账号
     */
    private String partner;
    /**
     * 报文签名类型
     */
    private String signType;
    /**
     * 密钥类型
     */
    private String keyType;
    /**
     * 证书文本内容
     */
    private String keyContext;
    /**
     * 证书到期时间
     */
    private Timestamp expireTime;
    /**
     * 状态 0-可用，1-不可用
     */
    private String status;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 备注信息
     */
    private String remark;
}