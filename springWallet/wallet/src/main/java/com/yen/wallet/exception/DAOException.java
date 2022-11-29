package com.yen.wallet.exception;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/exception/DAOException.java

public class DAOException extends RuntimeException{

    private final Integer code;

    public DAOException(Integer code, String message) {
        super(message); // TODO : double check it
        this.code = code;
    }

    public DAOException(Integer code, String message, Throwable e) {
        super(message, e); // TODO : double check it
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

}
