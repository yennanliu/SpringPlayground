package com.yen.springMySQL1.Repository;

import com.yen.springMySQL1.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{
}
