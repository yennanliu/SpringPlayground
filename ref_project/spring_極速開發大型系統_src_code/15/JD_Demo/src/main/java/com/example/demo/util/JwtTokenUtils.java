package com.example.demo.util;

/**
 * @author longzhonghua
 * @data 3/5/2019 11:58 PM
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;


public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    //秘鑰
    private static final String SECRET = "jwtSECRET";
    //簽發者
    private static final String ISS = "long";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";

    // 過期時間是3600秒，既是1個小時
    private static final long EXPIRATION = 3600L;

    // 選取了記住我之後的過期時間為7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    // 建立token
    public static String createToken(String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 從token中取得使用者名稱
    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    // 取得使用者角色
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已過期
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}