package com.yen.SpringAssignmentSystem.filter;

// https://github.com/yennanliu/SpringPlayground/blob/main/springBasics/springBootBasic1/src/main/java/com/yen/SpringBootPart1/filter/JwtTokenFilter.java

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

public class JwtTokenFilter extends OncePerRequestFilter {

    // for init testing
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        Authentication authentication = new TestingAuthenticationToken("principal", "credentials");
//        //authentication.setAuthenticated(true);
//        authentication.setAuthenticated(false);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<DecodedJWT> optionalDecodedJWT = Optional.ofNullable(request.getHeader("authorization"))
                .filter(s -> s.startsWith("Bearer ")).map(s -> s.substring(7)).map(s -> {
                    try {
                        System.out.println(">>> JWT.decode(s) = " + JWT.decode(s));
                        return JWT.decode(s);
                    } catch (JWTDecodeException ex) {
                        return null;
                    }
                });

        if (optionalDecodedJWT.isPresent()) {

            System.out.println(">>> optionalDecodedJWT = " + optionalDecodedJWT);
            System.out.println(">>> optionalDecodedJWT.get() = " + optionalDecodedJWT.get());

            Authentication authentication = new JwtAuthentication(optionalDecodedJWT.get());
            // 这里可以检查 JWT token 是否过期，issuer 等来设置 setAuthenticated(true/false)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}