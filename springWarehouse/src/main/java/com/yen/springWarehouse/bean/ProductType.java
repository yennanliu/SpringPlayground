package com.yen.springWarehouse.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@TableName("product_type")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 58533333369667604L;

    @TableId(type = IdType.AUTO)
    private int typeId;

    private String typeName;
}
