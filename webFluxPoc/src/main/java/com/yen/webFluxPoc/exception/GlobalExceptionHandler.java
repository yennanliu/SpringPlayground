package com.yen.webFluxPoc.exception;

// https://youtu.be/xUux3Ycjh7U?si=C-VE9ZxiF0rIcq2r&t=101

import org.springframework.web.bind.annotation.*;

/**
 *  Define global exception handler
 *
 *  @ResponseBody, @ControllerAdvice can be merged to @RestControllerAdvice
 */
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public String error(ArithmeticException exception){
        String msg = "math exception : " + exception;
        System.out.println(msg);
        return msg;
    }

}
