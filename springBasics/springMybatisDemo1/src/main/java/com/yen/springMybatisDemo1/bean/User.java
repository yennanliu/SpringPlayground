package com.yen.springMybatisDemo1.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private int id;

    private int age;

    private String name;

    private String baseLocation;
}
