package com.yen.SpringBootPart1.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/

@RestController
public class AuthController {

    // can be accessed without Auth
    @GetMapping("/public-api")
    public String publicApi(HttpServletRequest request) {

        /**
         *  Should print below:
         *      publicApi: SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@76301f75]
         *      publicApi: AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]
         */
        System.out.println("publicApi: " + request);
        System.out.println("publicApi: " + SecurityContextHolder.getContext().getAuthentication());

        return "this is a public api";
    }

    // can NOT be accessed without Auth
    @GetMapping("/private-api")
    public String privateApi(HttpServletRequest request) {

        System.out.println("privateApi:" + request);
        System.out.println("privateApi:" + SecurityContextHolder.getContext().getAuthentication());

        return "this is a private api";
    }

}

