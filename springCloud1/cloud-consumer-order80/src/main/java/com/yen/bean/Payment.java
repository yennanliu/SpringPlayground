package com.yen.bean;

// https://www.youtube.com/watch?v=4wWM7MmfxXw&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=10
// https://www.youtube.com/watch?v=8d6BvCZxPwQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=13

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** copy from cloud-provider-payment8001 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    private Long id;
    private String serial;
}
