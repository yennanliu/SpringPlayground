package com.yen.webFluxPoc.model;

// https://youtu.be/anguDoWURus?si=vLLY-9mKD2V5eztw&t=1762

public class Author {

    private Long id;
    private String name;

    public Author(){

    }

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}