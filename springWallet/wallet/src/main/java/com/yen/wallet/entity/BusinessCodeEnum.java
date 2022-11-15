package com.yen.wallet.entity;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/BusinessCodeEnum.java

public enum BusinessCodeEnum {

    /**
     * 电子账户基本信息管理返回码定义（1000开头，根据业务扩展 ...）
     */
    BUSI_ACCOUNT_FAIL_1000(1000, "该用户已开通该类型电子账户"),

    /**
     * 电子账户交易相关返回码定义（2000开头，根据业务扩展）
     */
    BUSI_CHARGE_FAIL_2000(2000, "充值失败"),
    BUSI_PAY_FAIL_2001(2001, "支付系统故障，请稍后重试");

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
