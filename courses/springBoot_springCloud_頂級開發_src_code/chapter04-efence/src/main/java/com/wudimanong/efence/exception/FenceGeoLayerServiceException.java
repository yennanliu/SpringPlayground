package com.wudimanong.efence.exception;

import com.alibaba.fastjson.JSON;

/**
 * @author jiangqiao
 */
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
