package com.yen.springSSO.config;

// book p. 3-26

import com.yen.springSSO.service.UserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserNameAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public void setUserDetailService(UserDetailService userDetailService) {
    }

    public void setHideUserNotFoundException(boolean b) {
    }

    public void setPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        return null;
    }

}
