package com.example.demo.config.exception;

public class CustomException extends RuntimeException {

    public CustomException() {
        super();
    }
    public CustomException(String message) {
        super(message);
    }
    public CustomException(Throwable cause) {
        super(cause);
    }
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
/*

使用
@Transactional(rollbackFor = Exception.class)

    throw new CustomException("密碼長度小於6位");
            throw new CustomException("使用者名稱 " + username + " 已被使用");*/
