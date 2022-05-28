package com.yen.springBootPOC2AdminSystem.bean;

import lombok.Data;
import lombok.ToString;

/** book p.71
 *
 *  Product Bean (mysql test)
 */

@Data
@ToString
public class Product {

    // attr
    private Long id;
    private String name;
    private String brand;
    private String madein;
    private int price;
}
