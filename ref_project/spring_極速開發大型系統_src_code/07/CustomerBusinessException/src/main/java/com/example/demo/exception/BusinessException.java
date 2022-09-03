package com.example.demo.exception;

/**
 * @author longzhonghua
 * @createdata 3/18/2019 10:26 PM
 * @description
 */
//@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR,reason="600")
public class BusinessException extends RuntimeException{
    //自訂錯誤碼
    private Integer code;
    //自訂建構器，必須輸入錯誤碼及內容
    public BusinessException(int code,String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}


/*
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Integer code;  //錯誤碼

    public BusinessException() {}

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
*/
