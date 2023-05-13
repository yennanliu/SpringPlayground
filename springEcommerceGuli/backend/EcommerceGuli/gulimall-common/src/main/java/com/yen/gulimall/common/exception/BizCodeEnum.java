package com.yen.gulimall.common.exception;

/**
 *  General error code enum
 *      - https://youtu.be/UT9lRWUwDGQ?t=668
 */
public enum BizCodeEnum {

    UNKNOWN_EXCEPTION(10000, "unknown system error"),
    VALID_EXCEPTION(10001, "para format validation failed"),

    // https://youtu.be/PZW2rOit2s8?t=754
    PRODUCT_UP_EXCEPTION(11000, "error when save to ES (product on shelf)");


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
