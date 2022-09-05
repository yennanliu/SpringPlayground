package com.example.demo.util.result;

import lombok.Data;

/**
 * @author longzhonghua
 * @data 2019/01/14 15:30
 */
@Data
public class Result<T> {

    // 錯誤碼
    private Integer code;

    //提示訊息
    private String msg;

    //實際內容
    private T data;


}
