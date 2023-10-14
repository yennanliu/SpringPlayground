package com.yen.springWarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private int id;

    private String name;

    private int typeId;

    private int price;

    private int merchantId;

    private String productStatus;

    private int amount;

    private String productTypeName;

    private String merchantName;
}
