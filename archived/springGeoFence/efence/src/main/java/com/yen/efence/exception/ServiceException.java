package com.yen.efence.exception;

// book p.4-31
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/exception/ServiceException.java

public class ServiceException extends RuntimeException{

    private final Integer code;

    public ServiceException(Integer code, String message) {
        super(message); // TODO: double check this
        this.code = code;
    }

    public ServiceException(Integer code, String message, Throwable e) {
        super(message, e); // TODO: double check this
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
