package com.yen.thymeleafDemo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetail {

    private int id;
    private String goodsName;
    private int price;
}
