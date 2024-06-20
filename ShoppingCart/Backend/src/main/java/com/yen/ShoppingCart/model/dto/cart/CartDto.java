package com.yen.ShoppingCart.model.dto.cart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private List<CartItemDto> cartItems;
    private double totalCost;
}
