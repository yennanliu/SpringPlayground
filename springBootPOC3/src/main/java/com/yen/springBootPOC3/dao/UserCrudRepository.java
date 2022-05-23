package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** book p. 77 */

public interface UserCrudRepository extends JpaRepository<User, Long> {
    List<User> finDByLastName(String lastname);
}
