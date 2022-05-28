package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
}
