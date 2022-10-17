package com.yen.springSSO.bean;

// book p.3-49

public class GlobalCodeEnum {

    private Integer code;
    private String desc;

    public static final GlobalCodeEnum GL_SUCCESS_0000 = null; // TODO: fix it
    public static final GlobalCodeEnum GL_FAIL_9999 = null; // TODO: fix it

    public static String getDesc() {
        return desc;
    }

    public static Integer getCode() {
        return code;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
