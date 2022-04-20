package com.yen.SpringBootPart1.bean;

// https://www.youtube.com/watch?v=aVNw04JjjSw&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=12

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * NOTE : we need @Component
 *      -> so container's component has SprirngBoot's support functionality
 */
//@Component
@ConfigurationProperties(prefix = "mycar") // will bind ALL setting (prefix = "mycar") in application.properties to this container
public class Car {

    private String brand;
    private Integer price;

    public Car(){

    }

    public String getBrand() {
        return brand;
    }

    public Integer getPrice() {
        return price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
