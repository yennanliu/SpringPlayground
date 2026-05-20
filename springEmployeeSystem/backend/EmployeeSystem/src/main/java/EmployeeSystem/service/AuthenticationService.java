package EmployeeSystem.service;

import EmployeeSystem.config.MessageStrings;
import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.repository.TokenRepository;
import EmployeeSystem.repository.UserRepository;
import EmployeeSystem.util.Helper;
import EmployeeSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  @Autowired TokenRepository repository;
  @Autowired UserRepository userRepository;
  @Autowired JwtUtil jwtUtil;

  // kept for backward-compat; not called by new JWT sign-in flow
  public void saveConfirmationToken(AuthenticationToken authenticationToken) {
    repository.save(authenticationToken);
  }

  public AuthenticationToken getToken(User user) {
    return repository.findTokenByUser(user);
  }

  /**
   * Validates the JWT, then returns the User — result is cached by the JWT string so
   * repeated calls within the 30-min TTL skip the DB lookup entirely.
   */
  @Cacheable(value = "tokens", key = "#token", unless = "#result == null")
  public User getUser(String token) {
    if (!jwtUtil.isTokenValid(token)) return null;
    Integer userId = jwtUtil.extractUserId(token);
    return userRepository.findById(userId).orElse(null);
  }

  /** Validates the JWT signature only — no DB hit. */
  public void authenticate(String token) throws AuthenticationFailException {
    if (!Helper.notNull(token)) {
      throw new AuthenticationFailException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
    }
    if (!jwtUtil.isTokenValid(token)) {
      throw new AuthenticationFailException(MessageStrings.AUTH_TOKEN_NOT_VALID);
    }
  }
}
