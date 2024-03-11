package com.ssafy.gallery.user.service;

import com.ssafy.gallery.auth.jwt.util.JwtUtil;
import com.ssafy.gallery.auth.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import com.ssafy.gallery.user.model.User;
import com.ssafy.gallery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final LoginTokenRepository loginTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public HashMap<String, String> login(OauthServerType oauthServerType, String authCode) {
        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.getDomain())
                .orElseGet(() -> userRepository.save(user));

        String accessToken = createToken("access");
        String refreshToken = createToken("refresh");
        saveTokens(accessToken, refreshToken, saved);

        HashMap<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    private String createToken(String type) {
        String secretKey = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        secretKey = secretKey.replace("-", "");
        long expiration = type.equals("access") ? accessExpiration : refreshExpiration;
        log.info("{} 토큰 만들기, secretKey: {}", type, secretKey);

        return JwtUtil.createJwt(secretKey, expiration);
    }

    private void saveTokens(String accessToken, String refreshToken, User saved) {
        log.info("Redis에 토큰 저장 : {}, {}", accessToken, refreshToken);
        LoginTokenDto accessTokenDto = new LoginTokenDto(accessToken, saved.getUserId(), null, accessExpiration);
        LoginTokenDto refreshTokenDto = new LoginTokenDto(refreshToken, saved.getUserId(), accessToken, refreshExpiration);
        loginTokenRepository.save(accessTokenDto);
        loginTokenRepository.save(refreshTokenDto);
    }
}