package com.yen.springWarehouse.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CREATE TABLE IF NOT EXISTS product_type(
 *
 *     type_id int PRIMARY KEY AUTO_INCREMENT,
 *     type_name varchar(30) not null
 * );
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {

    private int typeId;
    private String typeName;
}
