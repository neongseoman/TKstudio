package com.ssafy.gallery.auth.jwt.util;

import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final LoginTokenRepository loginTokenRepository;

    public static String createJwt(String secretKey, Long expiredMs) {
        Claims claims = Jwts.claims();
        claims.put("jti", UUID.randomUUID());

        return Jwts.builder()
                .setClaims(claims) // 만들어 놓은 claim을 넣는 것
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        token = token.split(" ")[1];
        log.info("token: {}", token);

        if (loginTokenRepository.findById(token).isPresent()) {
            LoginTokenDto loginTokenDto = loginTokenRepository.findById(token).get();
            log.info("로그인한 유저의 정보: {}", loginTokenDto);

            // accessToken이 아직 존재하면 비정상적인 접근 -> 다시 로그인 시키기
            String accessToken = loginTokenDto.getAccessToken();
            if (accessToken != null) {
                if (loginTokenRepository.findById(accessToken).isPresent()) {
                    loginTokenRepository.deleteById(accessToken);
                    loginTokenRepository.deleteById(token);
                    return null;
                }
            }

            return new UsernamePasswordAuthenticationToken(loginTokenDto.getUserId(), "", List.of(new SimpleGrantedAuthority("USER")));
        }

        return null;
    }

}