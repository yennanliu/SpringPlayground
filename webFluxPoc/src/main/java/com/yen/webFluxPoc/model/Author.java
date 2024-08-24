package com.yen.webFluxPoc.model;

// https://youtu.be/anguDoWURus?si=vLLY-9mKD2V5eztw&t=1762

import org.springframework.data.relational.core.mapping.Table;

@Table("author")
public class Author {

    //private Long id;
    private Integer id;
    private String name;

    public Author(){

    }

    public Author(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}