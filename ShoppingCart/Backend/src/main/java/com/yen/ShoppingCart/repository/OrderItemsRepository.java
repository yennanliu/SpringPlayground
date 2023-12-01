package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Integer> {
}