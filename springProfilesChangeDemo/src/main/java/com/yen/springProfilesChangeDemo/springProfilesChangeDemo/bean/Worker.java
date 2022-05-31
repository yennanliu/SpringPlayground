package com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean;

// https://www.youtube.com/watch?v=newMNCS4sik&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=82

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** NOTE !!! only env == dev, then activate Worker instance */
@Profile("dev")
@Component
@ConfigurationProperties("person")
@Data
public class Worker implements Person2 {

    private String name;
    private Integer age;
}
