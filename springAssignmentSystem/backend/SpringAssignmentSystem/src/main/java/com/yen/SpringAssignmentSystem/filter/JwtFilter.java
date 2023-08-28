package com.yen.SpringAssignmentSystem.filter;

// https://youtu.be/1Mn1AFs8eDo?si=FDQGqKiyehTP-xjQ&t=132
// https://youtu.be/1Mn1AFs8eDo?si=wE09Dh8KlrLN5kpP&t=1395
// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/filter/JwtFilter.java

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.repository.UserRepository;
import com.yen.SpringAssignmentSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (request.getCookies() == null) {
            chain.doFilter(request, response);
            return;
        }
        // Get authorization header and validate
        Optional<Cookie> jwtOpt = Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findAny();

        if (jwtOpt.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtOpt.get().getValue();
        UserDetails userDetails = null;
        try {
            Optional<User> appUserOpt = userRepository.findByUsername(jwtUtil.getUsernameFromToken(token));
            // TODO : fix below
//            userDetails = proffessoUserRepo
//                    .findByEmail(jwtUtil.getUsernameFromToken(token))
//                    .map(proffessoUser -> new User(proffessoUser, appUserOpt))
//                    .orElse(null);
        } catch (ExpiredJwtException | SignatureException e) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        if (!jwtUtil.validateToken(token, userDetails)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // this is where the authentication magic happens and the user is now valid!
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);

    }

}
