package com.yen.springMySQL1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Product {
    // attr
    private Long id;
    private String name;
    private String brand;
    private String madeIn;
    private float price;

    // constructor
    protected Product(){

    }

    // method
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId(){
        return id;
    }
}


