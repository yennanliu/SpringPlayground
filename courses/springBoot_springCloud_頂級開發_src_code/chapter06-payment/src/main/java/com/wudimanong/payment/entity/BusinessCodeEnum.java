package com.wudimanong.payment.entity;

/**
 * @author qiaojiang
 */

public enum BusinessCodeEnum {
    /**
     * 支付系统内部错误逻辑返回码定义（1000开头，根据业务扩展 ...）
     */
    BUSI_PAY_FAIL_1000(1000, "支付已成功，请勿重复支付"),
    BUSI_PAY_FAIL_1001(1001, "支付请求处理中，请稍后重试"),

    /**
     * 支付渠道错误码封装（2000开头，根据业务扩展）
     */
    BUSI_CHANNEL_FAIL_2000(2000, "支付宝报文组装错误");

    /**
     * 编码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;

    BusinessCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举类型
     *
     * @param code 编码
     * @return
     */
    public static BusinessCodeEnum getByCode(String code) {
        //判空
        if (code == null) {
            return null;
        }
        //循环处理
        BusinessCodeEnum[] values = BusinessCodeEnum.values();
        for (BusinessCodeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}