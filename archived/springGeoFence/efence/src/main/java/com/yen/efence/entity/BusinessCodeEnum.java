package com.yen.efence.entity;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/BusinessCodeEnum.java

public enum BusinessCodeEnum {
    /**
     * 图层业务层返回码定义（1000开头，根据业务扩展 ...）
     */
    BUSI_LAYER_FAIL_1000(1000, "图层信息已存在"),

    /**
     * 围栏操作业务层返回码定义（2000开头，根据业务扩展 ...)
     */
    BUSI_FENCE_FAIL_1000(2000, "围栏信息已存在");
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