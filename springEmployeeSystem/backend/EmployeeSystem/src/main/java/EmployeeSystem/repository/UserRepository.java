package EmployeeSystem.repository;

import EmployeeSystem.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface UserRepository extends R2dbcRepository<User, Integer> {

  // TODO : check if need implement ?
  //List<User> findAll();

  User findByEmail(String email);

  User findUserByEmail(String email);
}
