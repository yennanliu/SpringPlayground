package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@TableName("prduct")
public class Product implements Serializable {

    private static final long serialVersionUID = 88884233250815L;

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField("name")
    private String name;

    @TableField("type_id")
    private int typeId;

    @TableField("price")
    private int price;

    @TableField("product_status")
    private String productStatus;
}
