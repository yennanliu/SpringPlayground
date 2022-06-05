package com.vansl.sign.vo;

/**
 * @author: vansl
 * @create: 18-8-6 下午8:38
 */
public class HttpResult<T> {

    private String status;

    private String message;

    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
