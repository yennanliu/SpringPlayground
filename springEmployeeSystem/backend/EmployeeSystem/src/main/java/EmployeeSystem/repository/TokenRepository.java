package EmployeeSystem.repository;

import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {

  // TODO : check if need implement ?
  AuthenticationToken findTokenByUser(User user);

  AuthenticationToken findTokenByToken(String token);
}
