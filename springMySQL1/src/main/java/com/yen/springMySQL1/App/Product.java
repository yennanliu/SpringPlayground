package com.yen.springMySQL1.App;

import javax.persistence.*;

@Entity
public class Product {
    // attr
    private Long id;
    private String name;
    private String brand;
    private String madeIn;
    private float price;

    // constructor
    public Product(){

    }

    // method
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    //@GeneratedValue(strategy = GenerationType.AUTO)
//    public Long getId(){
//        return id;
//    }

    // getter, setter
//    public String getName() {
//        return name;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public String getMadeIn() {
//        return madeIn;
//    }
//
//    public float getPrice() {
//        return price;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public void setMadeIn(String madeIn) {
//        this.madeIn = madeIn;
//    }
//
//    public void setPrice(float price) {
//        this.price = price;
//    }
}


