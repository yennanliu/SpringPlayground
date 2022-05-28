package com.yen.springBootPOC2AdminSystem.exception;

// https://www.youtube.com/watch?v=TOwcNVQtniU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=56

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** custom Exception */

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason = "too much users") // via ResponseStatus, we can return error code, error msg...
public class UserTooManyException extends RuntimeException{

    // constructor
    public UserTooManyException(){

    }

    public UserTooManyException(String message){
        super(message);
    }

}
