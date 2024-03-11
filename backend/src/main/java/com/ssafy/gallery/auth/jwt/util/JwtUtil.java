package com.ssafy.gallery.auth.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    public static String createJwt(String secretKey, Long expiredMs) {
        Claims claims = Jwts.claims();
        claims.put("jti", String.valueOf(UUID.randomUUID()));

        return Jwts.builder()
                .setClaims(claims) // 만들어 놓은 claim을 넣는 것
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}