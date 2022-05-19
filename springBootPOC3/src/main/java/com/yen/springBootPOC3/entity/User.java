package com.yen.springBootPOC3.entity;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** book p.76 */

@Entity  // @Entity : label this si data entity class
@ToString
public class User {
    @Id  // @Id : make Id as PK (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // use auto increase strategy for id creation
    private Long id;
    private String firstName;
    private String lastName;

    protected User(){}
    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
