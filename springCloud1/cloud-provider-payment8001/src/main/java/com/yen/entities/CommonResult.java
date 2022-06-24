package com.yen.entities;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private Integer code;  // http status code : 404 not found, 200 OK ...
    private String message;
    private T data;

    public CommonResult(Integer code, String message){

        // method 1)
        //this(code, message, null);

        // method 2)
        this.code = code;
        this.message = message;
    }

}
