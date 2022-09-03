package com.wudimanong.experiment.client.entity;

/**
 * @author qiaojiang
 */

public enum BusinessCodeEnum {
    /**
     * 登录用户权限相关错误码（1000开头，根据业务扩展 ...）
     */
    BUSI_LOGIN_FAIL_1000(1000, "用户密码错误"),

    /**
     * 实验业务逻辑相关错误码（2000开头）
     */
    BUSI_LOGICAL_FAIL_2000(2000, "factor已存在实验"),
    BUSI_LOGICAL_LAYER_IS_NOT_EXIST(2001, "分层信息不存在"),
    BUSI_LOGICAL_OVER_AVAILABLE_FLOW(2002, "超出可用流量"),
    BUSI_LOGICAL_EXP_IS_NOT_EXIST(2003, "实验信息不存在");

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