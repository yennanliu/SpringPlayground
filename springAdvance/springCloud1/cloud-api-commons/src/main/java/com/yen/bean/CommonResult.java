package com.yen.bean;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10
// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13
// https://www.youtube.com/watch?v=D1pH2Ee88OM&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=15

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** copy from cloud-provider-payment8001 */

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
