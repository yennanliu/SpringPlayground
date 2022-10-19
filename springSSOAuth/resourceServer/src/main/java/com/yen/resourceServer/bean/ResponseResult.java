package com.yen.resourceServer.bean;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/entity/ResponseResult.java

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonPropertyOrder({"code", "message", "data"}) // TODO : check this usage
public class ResponseResult<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    /** return object */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data; // TODO : check this usage

    private Integer code;

    private String message;

    /** return success info */
    public static ResponseResult<String> OK(){
        return packageObject("", GlobalCodeEnum.GL_SUCCESS_0000);
    }

    /** return success info */
    public static <T> ResponseResult<T> OK(T data){
        return packageObject(data, GlobalCodeEnum.GL_SUCCESS_0000);
    }

    /** return unexpected exception/error info */
    public static <T> ResponseResult<T> systemException(){
        return packageObject(null, GlobalCodeEnum.GL_FAIL_9999);
    }

    /** return detected exception/error info */
    public static <T> ResponseResult<T> systemException(GlobalCodeEnum globalCodeEnum){
        return packageObject(null, globalCodeEnum);
    }

    /** wrap return message */
    public static <T> ResponseResult<T> packageObject(T data, GlobalCodeEnum globalCodeEnum) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(globalCodeEnum.getCode());
        responseResult.setMessage(globalCodeEnum.getDesc());
        responseResult.setData(data);
        return responseResult;
    }

}
