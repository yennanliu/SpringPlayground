package com.yen.springBootPOC2AdminSystem.exception;

// https://www.youtube.com/watch?v=TOwcNVQtniU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=56

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *  Handle whole web admin controller errors
 */

@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ArithmeticException.class, NullPointerException.class}) // handler for exceptions, deal with ArithmeticException, NullPointerException in this example
    public String handleArithException(Exception e){

        log.error(">>> Exception : {}", e);
        return "login"; // still return a view URL
    }

}
