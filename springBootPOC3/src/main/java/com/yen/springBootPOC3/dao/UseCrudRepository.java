package com.yen.springBootPOC3.dao;

import com.yen.springBootPOC3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/** book p. 77 */

public interface UserRepository extends JpaRepository<User, Long> {
}
