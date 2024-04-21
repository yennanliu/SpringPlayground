package com.yen.ShoppingCart.model.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private List<CartItemDto> cartItems;
    private double totalCost;
}
