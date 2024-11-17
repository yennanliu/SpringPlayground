package com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean;

// https://www.youtube.com/watch?v=newMNCS4sik&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=82

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("person")
public class Person {

    private String name;
    private Integer age;
}
