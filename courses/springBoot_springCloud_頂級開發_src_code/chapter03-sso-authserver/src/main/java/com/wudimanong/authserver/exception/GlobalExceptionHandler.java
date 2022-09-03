package com.wudimanong.authserver.exception;

import com.wudimanong.authserver.entity.GlobalCodeEnum;
import com.wudimanong.authserver.entity.ResponseResult;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qiaojiang
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一处理参数校验错误异常
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult<?> processValidException(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        List<String> errorStringList = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        String errorMessage = String.join("; ", errorStringList);
        response.setContentType("application/json;charset=UTF-8");
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.systemException(GlobalCodeEnum.GL_FAIL_9998);
    }

    /**
     * 统一处理参数校验错误异常
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult<?> processValidException(HttpServletResponse response, BindException e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        List<String> errorStringList = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        String errorMessage = String.join("; ", errorStringList);
        response.setContentType("application/json;charset=UTF-8");
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.systemException(GlobalCodeEnum.GL_FAIL_9998);
    }

    /**
     * 统一处理参数校验错误异常
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseResult<?> processValidException(HttpServletResponse response,
            HttpRequestMethodNotSupportedException e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String[] supportedMethods = e.getSupportedMethods();
        String errorMessage = "此接口不支持" + e.getMethod();
        if (!ArrayUtils.isEmpty(supportedMethods)) {
            errorMessage += "（仅支持" + String.join(",", supportedMethods) + "）";
        }
        response.setContentType("application/json;charset=UTF-8");
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.systemException(GlobalCodeEnum.GL_FAIL_9996);
    }

    /**
     * 非法授权异常统一处理
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(OAuth2Exception.class)
    @ResponseBody
    public ResponseResult<?> processOAuth2Exception(HttpServletResponse response, OAuth2Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.systemException(GlobalCodeEnum.BUSI_INVALID_GRANT);
    }

    /**
     * 未知系统异常处理方法
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<?> processDefaultException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        log.error(e.toString() + "_" + e.getMessage(), e);
        return ResponseResult.systemException();
    }
}
