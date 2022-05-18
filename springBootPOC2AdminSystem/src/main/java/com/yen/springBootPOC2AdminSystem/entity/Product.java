package com.yen.springBootPOC2AdminSystem.entity;

import lombok.Data;
import lombok.ToString;

/** book p.71 */

@Data
@ToString
public class Product {

    // attr
    private long id;
    private String name;
    private String brand;
    private String madein;
    private int price;
}
