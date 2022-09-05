package com.wudimanong.wallet.exception;

/**
 * @author jiangqiao
 */
public class DAOException extends RuntimeException {

    private final Integer code;

    public DAOException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public DAOException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
