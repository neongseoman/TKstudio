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
    private Long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public HashMap<String, String> login(OauthServerType oauthServerType, String authCode) {
        String secretKey = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        secretKey = secretKey.replace("-", "");
        log.info("secretKey: {}", secretKey);

        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.getDomain())
                .orElseGet(() -> userRepository.save(user));

        String accessToken = JwtUtil.createJwt(secretKey, accessExpiration);
        LoginTokenDto accessTokenDto = new LoginTokenDto(accessToken, saved.getUserId(), "access", accessExpiration);
        loginTokenRepository.save(accessTokenDto);

        String refreshToken = JwtUtil.createJwt(secretKey, refreshExpiration);
        LoginTokenDto refreshTokenDto = new LoginTokenDto(refreshToken, saved.getUserId(), "refresh", refreshExpiration);
        loginTokenRepository.save(refreshTokenDto);

        HashMap<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }
}