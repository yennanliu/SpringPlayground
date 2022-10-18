package com.yen.resourceServer.exception;

// book p.3-54

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
