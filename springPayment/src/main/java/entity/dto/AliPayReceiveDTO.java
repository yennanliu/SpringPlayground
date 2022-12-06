package entity.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/entity/dto/AliPayReceiveDTO.java

import lombok.Data;

@Data
public class AliPayReceiveDTO {

    /**
     * 通知时间(格式为yyyy-MM-dd HH:mm:ss)
     */
    private String notify_time;

    /**
     * 通知类型，例如：trade_status_sync
     */
    private String notify_type;

    /**
     * 通知校验ID
     */
    private String notify_id;

    /**
     * 格式编码，如utf-8
     */
    private String charset;

    /**
     * 接口版本，固定为1.0
     */
    private String version;

    /**
     * 签名类型，目前为RSA2
     */
    private String sign_type;

    /**
     * 签名信息
     */
    private String sign;

    /**
     * 授权方的app_id(由于本接口暂不开放第三方应用授权，因此auth_app_id=app_id)
     */
    private String auth_app_id;

    /**
     * 支付宝交易号
     */
    private String trade_no;

    /**
     * 开发者的app_id(支付宝分配给开发者的应用ID)
     */
    private String app_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 商户业务 ID，主要是退款通知中返回退款申请的流水号
     */
    private String out_biz_no;

    /**
     * 买家支付宝用户
     */
    private String buyer_id;

    /**
     * 卖家支付宝用户号
     */
    private String seller_id;

    /**
     * 交易状态，TRADE_SUCCESS-交易成功
     */
    private String trade_status;

    /**
     * 订单金额
     */
    private String total_amount;

    /**
     * 实收金额
     */
    private String receipt_amount;

    /**
     * 开票金额
     */
    private String invoice_amount;

    /**
     * 用户在交易中支付的金额，单位为元，精确到小数点后2位
     */
    private String buyer_pay_amount;

    /**
     * 使用集分宝支付的金额，单位为元，精确到小数点后2位
     */
    private String point_amount;

    /**
     * 退款通知中，返回总退款金额，单位为元，精确到小数点后2位
     */
    private String refund_fee;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易创建时间,格式为yyyy-MM-dd HH:mm:ss
     */
    private String gmt_create;

    /**
     * 交易付款时间,格式为yyyy-MM-dd HH:mm:ss
     */
    private String gmt_payment;

    /**
     * 交易退款时间,格式为yyyy-MM-dd HH:mm:ss
     */
    private String gmt_refund;

    /**
     * 交易结束时间,格式为yyyy-MM-dd HH:mm:ss
     */
    private String gmt_close;

    /**
     * 支付成功的各个渠道金额信息，格式为[{}]
     */
    private String fund_bill_list;

    /**
     * 优惠券信息，格式为[{}]
     */
    private String voucher_detail_list;

    /**
     * 公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回
     */
    private String passback_params;

    //receipt_amount、invoice_amount、buyer_pay_amount、point_amount、voucher_detail_list 等参数在用户使用优惠券时才会返回
}