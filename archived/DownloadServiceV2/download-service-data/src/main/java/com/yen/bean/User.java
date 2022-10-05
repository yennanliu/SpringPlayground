package com.yen.bean;

// https://kucw.github.io/blog/2020/2/spring-unit-test-mockito/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private int age;
}
