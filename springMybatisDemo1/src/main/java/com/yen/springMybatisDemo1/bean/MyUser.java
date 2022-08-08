package com.yen.springMybatisDemo1.bean;

// https://www.youtube.com/watch?v=xVdgzSkU6po&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=10

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyUser {

    private String username;

    private String password;

    private Integer age;

    private String sex;

    private String email;
}
