package com.yen.gulimall.product.exception;

import com.yen.gulimall.common.exception.BizCodeEnum;
import com.yen.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  Handle all exceptions
 *      - https://youtu.be/UT9lRWUwDGQ?t=163
 */


/** handle MethodArgumentNotValidException */
@Slf4j
@RestControllerAdvice(basePackages = "com.yen.gulimall.product.controller") // RestControllerAdvice equals as @ResponseBody + @ControllerAdvice
//@ResponseBody // return as JSON format
//@ControllerAdvice(basePackages = "com.yen.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();

        bindingResult.getFieldErrors().forEach((fieldError) -> {
            String field = fieldError.getField();
            String msg = fieldError.getDefaultMessage();
            errorMap.put(field, msg);
        });

        log.error("data validation exception : {}, exception type : {}", e.getMessage(), e.getClass());
        //return R.error(400, "data validation check failed").put("data", errorMap);
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    /** handle other type of exceptions */
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        //return R.error(400, "Exception").put("data", throwable);
        log.error(">>> error = {}", throwable);
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg()).put("data", throwable);
    }

}
