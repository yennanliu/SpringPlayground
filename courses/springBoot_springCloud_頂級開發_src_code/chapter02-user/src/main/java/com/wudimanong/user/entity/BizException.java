package com.wudimanong.user.entity;

import javax.servlet.ServletException;
import lombok.Data;
import lombok.ToString;

/**
 * @author joe
 */
@Data
@ToString
public class BizException extends ServletException {

    private int code;

    private String message;

    public BizException() {
    }

    public BizException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(int code, Throwable rootCause) {
        super(rootCause);
        this.code = code;
        this.message = rootCause.getMessage();
    }

    public BizException(int code, String message, Throwable rootCause) {
        super(rootCause);
        this.code = code;
        this.message = message;
    }
}
