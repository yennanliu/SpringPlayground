package com.yen.gulimall.product.exception;

import com.yen.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 *  Handle all exceptions
 *      - https://youtu.be/UT9lRWUwDGQ?t=163
 */


@Slf4j
@RestControllerAdvice(basePackages = "com.yen.gulimall.product.controller") // RestControllerAdvice equals as @ResponseBody + @ControllerAdvice
//@ResponseBody // return as JSON format
//@ControllerAdvice(basePackages = "com.yen.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public R handleValidException(Exception e){

        log.error("data validation exception : {}, exception type : {}", e.getMessage(), e.getClass());
        return R.error();
    }

}
