package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    // TODO : fix below
    // https://github.com/yennanliu/til?tab=readme-ov-file#20240215
    //@Query(value = "SELECT * FROM Products p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    //List<Product> searchProductsByName(@Param("name") String name);
}