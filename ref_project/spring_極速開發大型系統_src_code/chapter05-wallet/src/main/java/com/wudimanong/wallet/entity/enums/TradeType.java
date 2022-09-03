package com.wudimanong.wallet.entity.enums;

import com.wudimanong.wallet.entity.BusinessCodeEnum;

/**
 * @author jiangqiao
 */
public enum TradeType {
    CHARGE("charge", "充值");
    /**
     * 枚举值
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    TradeType(String code, String desc) {
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

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
