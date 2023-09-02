package com.yen.SpringBootPart1.filter;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class JwtAuthentication extends AbstractAuthenticationToken {

    /**
     *  Dep from :
     *      <dependency>
     *      <groupId>com.auth0</groupId>
     *      <artifactId>java-jwt</artifactId>
     *      <version>3.19.2</version>
     *      </dependency>
     */
    private final DecodedJWT decodedJWT;

    public JwtAuthentication(DecodedJWT decodedJWT) {

        super(null);
        this.decodedJWT = decodedJWT;
        setAuthenticated(decodedJWT.getExpiresAt().after(new Date()));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(decodedJWT.getClaim("roles").asList(String.class))
                .map(roles -> roles.stream().map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                        .collect(toList()))
                .orElse(emptyList());
    }

    @Override
    public Object getPrincipal() {
        return decodedJWT;
    }


}