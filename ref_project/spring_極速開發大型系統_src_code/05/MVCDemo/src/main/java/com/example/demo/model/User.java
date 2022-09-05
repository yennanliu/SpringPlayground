package com.example.demo.model;
import lombok.Data;
/**
 * Author:   longzhonghua
 * Date:     3/22/2019 10:42 AM
 */
@Data
public class User {
    //定義id
    private long id;
    //定義使用者名稱
    private String name;
    //定義使用者年齡
    private  int age;
}
