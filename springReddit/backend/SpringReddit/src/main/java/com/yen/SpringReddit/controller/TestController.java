package com.yen.SpringReddit.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello !!!";
    }

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
    /**
     *  Example curl request (work)
     *
     *  JWT encode <--> decode : https://jwt.io
     *
     *   V1 :
     *   cmd : curl -i -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJleHAiOjE5MTQ1ODc1OTV9.xcdGByP5GcqOA8Ah_iNGEb1WFnnxUyvqSmKTAsiGryE" -i http://localhost:8888/private-api
     *
     *   JWT sharing : https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJleHAiOjE5MTQ1ODc1OTV9.xcdGByP5GcqOA8Ah_iNGEb1WFnnxUyvqSmKTAsiGryE
     *
     *   V2 : (with role = ROLE_contributors)
     *   cmd :  curl -i -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJleHAiOjE5MTQ1ODc1OTUsInJvbGVzIjpbIlJPTEVfY29udHJpYnV0b3JzIl19.6mGJpTYHJoaJhjFgaf6C7QLbTf4v2MoDjBS_fnqNJkE" -i http://localhost:8888/private-api
     *
     *
     */
    @GetMapping("/private-api")
    public String privateApi(HttpServletRequest request) {

        System.out.println("privateApi:" + request);
        System.out.println("privateApi:" + SecurityContextHolder.getContext().getAuthentication());

        return "this is a private api";
    }

    // https://yanbin.blog/springboot-security-jwt-token-how-to-abcs/
    // cat get decodeJWT in controller via @AuthenticationPrincipal, otherwise, can only get it in JwtTokenFilter
    @GetMapping("/private-api-2")
    public String privateApi_2(HttpServletRequest request, @AuthenticationPrincipal DecodedJWT decodedJWT) {
        System.out.println("privateApi decodeJWT: " + decodedJWT);
        System.out.println("privateApi request: " + request);
        System.out.println("privateApi authentication: " + SecurityContextHolder.getContext().getAuthentication());
        return "this is a private api 2";
    }

}
