package com.yen.ShoppingCart.repository;

import com.yen.ShoppingCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // TODO : check if need implement ?
    List<User> findAll();

    User findByEmail(String email);

    User findUserByEmail(String email);
}