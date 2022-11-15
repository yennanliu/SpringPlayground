package com.yen.wallet.entity;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/ResponseResult.java

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
@JsonPropertyOrder({"code", "message", "data"}) // TODO : double check it
public class  ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL) // TODO : double check it
    private T data;

    private Integer code;

    /**
     * 返回的信息
     */
    private String message;

    /**
     * 成功返回成功响应码
     *
     * @return 响应结果
     */
    public static ResponseResult<String> OK() {
        return packageObject("", GlobalCodeEnum.GL_SUCC_0000);
    }

    /**
     * 成功返回响应数据
     *
     * @param data 返回的数据
     * @param <T>  返回的数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> OK(T data) {
        return packageObject(data, GlobalCodeEnum.GL_SUCC_0000);
    }

    /**
     * 对返回的消息进行包装
     *
     * @param data           返回的数据
     * @param globalCodeEnum 自定义的返回码枚举类型
     * @param <T>            返回的数据类型
     * @return 响应结果
     */
    /** NOTE THIS !!! */
    public static <T> ResponseResult<T> packageObject(T data, GlobalCodeEnum globalCodeEnum) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(globalCodeEnum.getCode());
        responseResult.setMessage(globalCodeEnum.getDesc());
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 系统发生异常不可用时返回
     *
     * @param <T> 返回的数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> systemException() {
        return packageObject(null, GlobalCodeEnum.GL_FAIL_9999);
    }

    /**
     * 发现可感知的系统异常时返回
     *
     * @param globalCodeEnum
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> systemException(GlobalCodeEnum globalCodeEnum) {
        return packageObject(null, globalCodeEnum);
    }

    /**
     * 远程服务熔断降级结果封装
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> serviceException(Integer code, String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();

        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

}
