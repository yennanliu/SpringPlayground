package EmployeeSystem.demo.repository;

import EmployeeSystem.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
