package com.yen.SpringAssignmentSystem.filter;

// https://github.com/yennanliu/SpringPlayground/blob/main/springBasics/springBootBasic1/src/main/java/com/yen/SpringBootPart1/filter/JwtAuthentication.java

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Date;

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
    public Object getPrincipal() {
        return decodedJWT;
    }
}