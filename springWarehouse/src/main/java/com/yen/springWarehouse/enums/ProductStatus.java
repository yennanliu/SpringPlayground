package com.yen.springWarehouse.enums;

public enum ProductStatus {

    New(20000, "new"),
    onBoard(20001, "on_board"),
    offBoard(20002, "off_board");

    private int code;
    private String msg;

    ProductStatus(int code, String msg){
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
