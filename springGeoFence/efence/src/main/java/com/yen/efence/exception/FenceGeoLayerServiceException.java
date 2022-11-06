package com.yen.efence.exception;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/exception/FenceGeoLayerServiceException.java

import com.alibaba.fastjson.JSON;

public class FenceGeoLayerServiceException extends ServiceException {

    /**
     * 定义图层业务层方法异常信息中具体携带的业务对象
     */
    private Object object;

    /**
     * 不带原始异常对象的业务异常构建方法（用于业务异常处理）
     *
     * @param code
     * @param message
     * @param object
     */
    public FenceGeoLayerServiceException(Integer code, String message, Object object) {
        super(code, message);
        this.object = object;
    }

    /**
     * 携带原始异常对象信息的业务异常构建方法（用于业务层俘获未知异常时抛出原始异常信息）
     *
     * @param code
     * @param message
     * @param object
     * @param e
     */
    public FenceGeoLayerServiceException(Integer code, String message, Object object, Throwable e) {
        super(code, message, e);
        this.object = object;
    }

    /**
     * 异常对象toString方法
     *
     * @return
     */
    @Override
    public String toString() {
        return JSON.toJSONString(object);
    }
}
