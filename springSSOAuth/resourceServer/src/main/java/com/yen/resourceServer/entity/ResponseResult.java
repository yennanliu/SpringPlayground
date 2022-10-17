package com.yen.resourceServer.entity;

// book 3-46

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yen.resourceServer.bean.GlobalCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"code", "message", "data"})
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** return object */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /** return code */
    private Integer code;

    /** return description info */
    private String message;

    /** return success code */
    private static ResponseResult<String> OK(){
        return packageObject("", GlobalCodeEnum.GL_SUCCESS_0000);
    }

    /** return data */
    private static <T> ResponseResult<T> OK(T data){
        return packageObject(data, GlobalCodeEnum.GL_SUCCESS_0000);
    }

    /** wrap return data */
    public static <T> ResponseResult<T> packageObject(T data, GlobalCodeEnum globalCodeEnum){

        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(globalCodeEnum.getCode());
        responseResult.setMessage(globalCodeEnum.getDesc());
        responseResult.setData(data);
        return responseResult;
    }

    /** return when system in abnormal (not accessible) status */
    public static <T> ResponseResult<T> systemException(){

        return packageObject(null, GlobalCodeEnum.GL_FAIL_9999);
    }

    /** return catchable system error */
    public static <T> ResponseResult<T> systemException(GlobalCodeEnum globalCodeEnum){

        return packageObject(null, globalCodeEnum);
    }

}
