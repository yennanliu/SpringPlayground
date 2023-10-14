package com.yen.springWarehouse.enums;

public enum OrderStatus {

    Completed(10000, "completed"),

    Pending(10001, "pending"),

    Cancelled(10002, "cancelled");


    private int code;
    private String msg;

    OrderStatus(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }

}
