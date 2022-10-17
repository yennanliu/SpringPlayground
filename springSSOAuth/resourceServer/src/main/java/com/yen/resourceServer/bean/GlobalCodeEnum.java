package com.yen.resourceServer.bean;

// book 3-49
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/entity/GlobalCodeEnum.java

public enum GlobalCodeEnum {
    /**
     * 全局返回码定义
     */
    GL_SUCCESS_0000(0, "成功"),
    GL_FAIL_9996(996, "不支持的HttpMethod"),
    GL_FAIL_9997(997, "HTTP错误"),
    GL_FAIL_9998(998, "参数错误"),
    GL_FAIL_9999(999, "系统异常"),

    /**
     * 业务逻辑异常码定义
     */
    BUSI_USER_NOT_EXIST(1001, "用户信息不存在");
    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    GlobalCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举类型
     *
     * @param code 编码
     * @return
     */
    public static GlobalCodeEnum getByCode(String code) {
        //判空
        if (code == null) {
            return null;
        }
        //循环处理
        GlobalCodeEnum[] values = GlobalCodeEnum.values();
        for (GlobalCodeEnum value : values) {
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