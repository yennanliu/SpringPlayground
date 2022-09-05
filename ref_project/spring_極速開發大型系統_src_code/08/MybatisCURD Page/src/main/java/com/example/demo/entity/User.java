package com.example.demo.entity;

import lombok.Data;

/**
 * @author longzhonghua
 * @data 2/19/2019 8:24 PM
 */
//User（不同於JPA的點，不需要映射.JPA需要註釋@entity）
@Data
public class User {
    private int id;
    private String name;
    private int age;
}