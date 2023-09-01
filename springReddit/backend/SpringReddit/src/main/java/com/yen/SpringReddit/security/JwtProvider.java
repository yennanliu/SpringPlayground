//package com.yen.SpringReddit.security;
//
//// https://youtu.be/1ojKQxVssPQ?t=610
//
//// V1
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/master/src/main/java/com/programming/techie/springredditclone/security/JwtProvider.java
//
//// V2
//// https://github.com/SaiUpadhyayula/spring-reddit-clone/blob/revert-16-patch-1/src/main/java/com/programming/techie/springredditclone/security/JwtAuthenticationFilter.java
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//
//@Service
//@RequiredArgsConstructor
//public class JwtProvider {
//
////    @Autowired
////    private final JwtEncoder jwtEncoder;
//    @Value("${jwt.expiration.time}")
//    private Long jwtExpirationInMillis;
//
//    public String generateToken(Authentication authentication) {
//        User principal = (User) authentication.getPrincipal();
//        return generateTokenWithUserName(principal.getUsername());
//    }
//
//    public String generateTokenWithUserName(String username) {
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(Instant.now())
//                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
//                .subject(username)
//                .claim("scope", "ROLE_USER")
//                .build();
//
//        //return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//        return JwtEncoderParameters.from(claims).toString();
//    }
//
//    public Long getJwtExpirationInMillis() {
//        return jwtExpirationInMillis;
//    }
//}
//
//
////import com.yen.SpringReddit.exceptions.SpringRedditException;
//////import io.jsonwebtoken.Claims;
//////import io.jsonwebtoken.Jwts;
////import jakarta.annotation.PostConstruct;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.stereotype.Service;
////
//////import javax.annotation.PostConstruct;
////import java.io.IOException;
////import java.io.InputStream;
////import java.security.*;
////import java.security.cert.CertificateException;
////import java.sql.Date;
////import java.time.Instant;
////
//////import static io.jsonwebtoken.Jwts.parser;
////import static java.util.Date.from;
////
////@Service
////public class JwtProvider {
////
////    private KeyStore keyStore;
////    @Value("${jwt.expiration.time}")
////    private Long jwtExpirationInMillis;
////
////    @PostConstruct
////    public void init() {
////        try {
////            keyStore = KeyStore.getInstance("JKS");
////            InputStream resourceAsStream = getClass().getResourceAsStream("/spring_reddit.jks");
////            keyStore.load(resourceAsStream, "secret".toCharArray());
////        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
////            throw new SpringRedditException("Exception occurred while loading keystore", e);
////        }
////
////    }
////
////    public String generateToken(Authentication authentication) {
////        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
////        return Jwts.builder()
////                .setSubject(principal.getUsername())
////                .setIssuedAt(from(Instant.now()))
////                .signWith(getPrivateKey())
////                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
////                .compact();
////    }
////
////    public String generateTokenWithUserName(String username) {
////        return Jwts.builder()
////                .setSubject(username)
////                .setIssuedAt(from(Instant.now()))
////                .signWith(getPrivateKey())
////                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
////                .compact();
////    }
////
////    private PrivateKey getPrivateKey() {
////        try {
////            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
////        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
////            throw new SpringRedditException("Exception occured while retrieving public key from keystore", e);
////        }
////    }
////
////    public boolean validateToken(String jwt) {
////        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
////        return true;
////    }
////
////    private PublicKey getPublickey() {
////        try {
////            return keyStore.getCertificate("springblog").getPublicKey();
////        } catch (KeyStoreException e) {
////            throw new SpringRedditException("Exception occured while " +
////                    "retrieving public key from keystore", e);
////        }
////    }
////
////    public String getUsernameFromJwt(String token) {
////        Claims claims = parser()
////                .setSigningKey(getPublickey())
////                .parseClaimsJws(token)
////                .getBody();
////
////        return claims.getSubject();
////    }
////
////    public Long getJwtExpirationInMillis() {
////        return jwtExpirationInMillis;
////    }
////
////}
//
//
////
////import com.yen.SpringReddit.exceptions.SpringRedditException;
////import io.jsonwebtoken.Jwts;
////import jakarta.annotation.PostConstruct;
////import lombok.RequiredArgsConstructor;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.context.annotation.Bean;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.security.oauth2.jwt.JwtClaimsSet;
////import org.springframework.security.oauth2.jwt.JwtEncoder;
////import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
////import org.springframework.stereotype.Service;
////
////import java.io.IOException;
////import java.io.InputStream;
////import java.security.*;
////import java.security.cert.CertificateException;
////import java.time.Instant;
////
////@Service
//////@RequiredArgsConstructor
////public class JwtProvider {
////
//////    @Autowired
//////    private JwtEncoder jwtEncoder;
////
////    private KeyStore keyStore;
////
////    // read from conf
////    @Value("${jwt.expiration.time}")
////    private Long jwtExpirationInMillis;
////
//////    @PostConstruct
//////    public void init(){
//////
//////        try{
//////            keyStore =  KeyStore.getInstance("JKS");
//////            InputStream resourceStream = getClass().getResourceAsStream("/spring_reddit.jks");
//////            keyStore.load(resourceStream, "secret".toCharArray());
//////        }catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
//////            throw new SpringRedditException("Exception when load keystore");
//////        }
//////
//////    }
////
////
////    public String generateToken(Authentication authentication){
////
////        User principal = (User) authentication.getPrincipal();
////        //return generateTokenWithUserName(principal.getUsername());
////        return Jwts.builder()
////                .setSubject(principal.getUsername())
////                .signWith(getPrivateKey())
////                .compact();
////    }
////
////
////    public String generateTokenWithUserName(String username) {
////
////        JwtClaimsSet claims = JwtClaimsSet.builder()
////                .issuer("self")
////                .issuedAt(Instant.now())
////                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
////                .subject(username)
////                .claim("scope", "ROLE_USER")
////                .build();
////
////        // TODO : fix below
////        //return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
////        return claims.toString();
////    }
////
////    public Long getJwtExpirationInMillis() {
////        return jwtExpirationInMillis;
////    }
////
////    private PrivateKey getPrivateKey(){
////
////        try {
////            return (PrivateKey) keyStore.getKey("spring_reddit", "secret".toCharArray());
////        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
////            throw new SpringRedditException("Exception when retrieving public key from keystore");
////        }
////    }
////
////}
