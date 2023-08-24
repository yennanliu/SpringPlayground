//package com.yen.SpringReddit.security;
//
//// https://youtu.be/PN3gxkub4Ew
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/revert-16-patch-1/src/main/java/com/programming/techie/springredditclone/security/JwtAuthenticationFilter.java
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter{
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String jwt = getJwtFromRequest(request);
//
//        if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)){
//
//            String username = jwtProvider.getUsernameFromJwt(jwt);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(userDetails,
//                    null, userDetails.getAuthorities());
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
//
//        // TODO : check what's this for
//        filterChain.doFilter(request, response);
//    }
//
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return bearerToken;
//    }
//
//}
