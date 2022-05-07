package com.yen.SpringBootPart1.bean;

// https://www.youtube.com/watch?v=rGgzYESEe84&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=36

import lombok.Data;

import java.util.Date;

@Data // NOTE: we need this annotation, so this Person2 can be used by controller
public class Person2 {

    private String userName;
    private Integer age;
    private Date birth;
    private Pet pet;
}
