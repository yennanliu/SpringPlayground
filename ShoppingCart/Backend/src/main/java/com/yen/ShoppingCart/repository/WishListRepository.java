package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

    // TODO : check if need to implement ??
    List<WishList> findAllByUserIdOrderByCreatedDateDesc(Integer userId);
}
