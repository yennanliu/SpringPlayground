package com.yen.springBootPOC2AdminSystem.bean;

// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66

/**
 *  User2 Bean (myBatis plus test)
 */

import lombok.Data;

@Data
public class User2 {

    // attr
    private Long id;
    private String name;
    private int age;
    private String email;
}
