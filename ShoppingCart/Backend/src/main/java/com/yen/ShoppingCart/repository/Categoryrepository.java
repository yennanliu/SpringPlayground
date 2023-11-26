package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Categoryrepository extends JpaRepository<Category, Integer> {

    public Category findByCategoryName(String categoryName);

}