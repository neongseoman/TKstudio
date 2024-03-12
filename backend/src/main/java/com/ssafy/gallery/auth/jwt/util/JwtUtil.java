package com.ssafy.gallery.auth.jwt.util;

import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.access.expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public String createToken(String type) {
        String secretKey = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        secretKey = secretKey.replace("-", "");
        long expiration = type.equals("access") ? accessExpiration : refreshExpiration;
        log.info("{} 토큰 만들기, secretKey: {}", type, secretKey);

        Claims claims = Jwts.claims();
        claims.put("jti", UUID.randomUUID());

        return Jwts.builder()
                .setClaims(claims) // 만들어 놓은 claim을 넣는 것
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    public void saveTokens(String accessToken, String refreshToken, int userId) {
        log.info("Redis에 토큰 저장 : {}, {}", accessToken, refreshToken);
        LoginTokenDto accessTokenDto = new LoginTokenDto(accessToken, userId, null, accessExpiration);
        LoginTokenDto refreshTokenDto = new LoginTokenDto(refreshToken, userId, accessToken, refreshExpiration);
        loginTokenRepository.save(accessTokenDto);
        loginTokenRepository.save(refreshTokenDto);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response, String token) {
        token = token.split(" ")[1];
        log.info("getAuthentication: {}", token);

        if (loginTokenRepository.findById(token).isPresent()) {
            LoginTokenDto loginTokenDto = loginTokenRepository.findById(token).get();
            log.info("로그인한 유저의 정보: {}", loginTokenDto);

            String accessToken = loginTokenDto.getAccessToken();
            if (accessToken != null) {
                if (loginTokenRepository.findById(accessToken).isPresent()) {
                    // accessToken이 아직 유효하면 비정상적인 접근 -> 다시 로그인 시키기
                    log.info("로그아웃: 엑세스 토큰, 리프레시 토큰 삭제");

                    loginTokenRepository.deleteById(accessToken);
                    loginTokenRepository.deleteById(token);
                    return null;
                } else {
                    // accessToken이 만료되었으면 둘 다 재발급
                    log.info("리프레시 토큰 로그인: 토큰 재발급");
                    loginTokenRepository.deleteById(token);

                    accessToken = createToken("access");
                    String refreshToken = createToken("refresh");
                    saveTokens(accessToken, refreshToken, loginTokenDto.getUserId());

                    response.setHeader("accessToken", accessToken);
                    response.setHeader("refreshToken", refreshToken);
                    request.setAttribute("refreshToken", refreshToken);
                }
            }

            return new UsernamePasswordAuthenticationToken(loginTokenDto.getUserId(), "", List.of(new SimpleGrantedAuthority("USER")));
        }

        return null;
    }

}