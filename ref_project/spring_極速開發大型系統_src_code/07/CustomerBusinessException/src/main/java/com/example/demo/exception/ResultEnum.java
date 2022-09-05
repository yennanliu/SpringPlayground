﻿package com.example.demo.exception;

/**
 * @author longzhonghua
 * @createdata 3/18/2019 11:36 PM
 * @description
 */
public enum ResultEnum {
    UNKONW_ERROR(-1,"不詳錯誤"),
    SUCCESS(0,"成功"),
    ERROR(1,"失敗"),
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
