package EmployeeSystem.repository;

import EmployeeSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    // TODO : check if need implement ?
    List<User> findAll();

    User findByEmail(String email);

    User findUserByEmail(String email);
}
