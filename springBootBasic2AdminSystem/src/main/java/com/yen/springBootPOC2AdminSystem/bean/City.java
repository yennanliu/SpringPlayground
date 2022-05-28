package com.yen.springBootPOC2AdminSystem.bean;

// https://www.youtube.com/watch?v=oJGcVUf4rEM&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=65

import lombok.Data;

/**
 *  City Bean  (myBatis test)
 */

@Data
public class City {

    // attr
    private Long id;
    private String name;
    private String state;
    private String country;
}
