package com.yen.springBootPOC3.entity;

import lombok.Data;

/** book p.57 */

@Data
public class Person {

    // attr
    private String name;
    private Integer age;
    private String address;

    // constructor
    public Person(){
        super();
    }

    public Person(String name, Integer age, String address){
        this.name = name;
        this.age = age;
        this.address = address;
    }

}
