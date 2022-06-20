package com.yen.springSwaggerDemo1.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private int id;
    private String prodName;
    private Date createDay;
    private String type;
}
