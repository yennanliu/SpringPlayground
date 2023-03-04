package com.yen.gulimall.common.exception;

/**
 *  General error code enum
 *      - https://youtu.be/UT9lRWUwDGQ?t=668
 */
public enum BizCodeEnum {

    UNKNOWN_EXCEPTION(10000, "unknown system error"),
    VALID_EXCEPTION(10001, "para format validation failed");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

}
