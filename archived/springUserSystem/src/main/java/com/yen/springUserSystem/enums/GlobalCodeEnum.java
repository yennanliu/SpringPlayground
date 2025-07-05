package com.yen.springUserSystem.enums;

// book p. 3-49

import lombok.Getter;

@Getter
public enum GlobalCodeEnum {

    GL_SUCC_0000(0, "success"),

    GL_FAIL_9996(996, "not support http method"),

    GL_FAIL_9997(997, "http error"),

    GL_FAIL_9998(998, "param error"),

    GL_FAIL_9999(999, "system error"),
    ;

    private Integer code;
    private String desc;

    GlobalCodeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static GlobalCodeEnum getByCode(String code){
        if (code == null){
            return null;
        }
        GlobalCodeEnum[] values = GlobalCodeEnum.values();
        for (GlobalCodeEnum value : values){
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }

}
