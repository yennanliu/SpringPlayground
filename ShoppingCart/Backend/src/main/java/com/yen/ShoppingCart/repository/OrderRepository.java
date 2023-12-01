package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.Order;
import com.yen.ShoppingCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer> {

    List<Order> findAllByUserOrderByCreatedDateDesc(User user);

}