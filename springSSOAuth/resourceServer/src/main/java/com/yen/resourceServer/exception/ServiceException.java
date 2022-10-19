package com.yen.resourceServer.exception;

// book p.3-54
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/exception/ServiceException.java

public class ServiceException extends RuntimeException{

    private Integer code;

    public ServiceException(Integer code, String message){

        super(message); // TODO : check this using
        this.code = code;
    }

    public ServiceException(Integer code, String message, Throwable e){

        super(message, e); // TODO : check this using
        this.code = code;
    }

    public Integer getCode(){

        return code;
    }

}
