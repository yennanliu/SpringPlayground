package com.yen.ShoppingCart.model.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CheckoutItemDto {

    private String productName;
    private int  quantity;
    private double price;
    private long productId;
    private int userId;

}
