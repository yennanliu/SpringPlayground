package com.yen.springWarehouse.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CREATE TABLE IF NOT EXISTS product(
 *     id int PRIMARY KEY AUTO_INCREMENT,
 *     name varchar(100) not null,
 *     type_id int not null,
 *     price int not null,
 *     product_status varchar(1) not null,
 *     constraint FK_PRODUCT_TYPE FOREIGN KEY (type_id) references product_type(type_id)
 * );
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String name;
    private int typeId;
    private int price;
    private String productStatus;
}
