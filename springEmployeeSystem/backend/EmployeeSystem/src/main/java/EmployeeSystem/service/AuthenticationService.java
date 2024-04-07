package EmployeeSystem.service;

import EmployeeSystem.config.MessageStrings;
import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.repository.TokenRepository;
import EmployeeSystem.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/service/AuthenticationService.java

@Service
public class AuthenticationService {

    @Autowired
    TokenRepository repository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {

        repository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {

        return repository.findTokenByUser(user);
    }

    public User getUser(String token) {

        AuthenticationToken authenticationToken = repository.findTokenByToken(token);
        if (Helper.notNull(authenticationToken)) {
            if (Helper.notNull(authenticationToken.getUser())) {
                return authenticationToken.getUser();
            }
        }
        return null;
    }

    public void authenticate(String token) throws AuthenticationFailException {

        if (!Helper.notNull(token)) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
        }
        if (!Helper.notNull(getUser(token))) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOKEN_NOT_VALID);
        }
    }

}
