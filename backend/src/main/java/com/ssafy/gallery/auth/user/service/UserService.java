package com.ssafy.gallery.auth.user.service;

import com.ssafy.gallery.auth.jwt.util.JwtUtil;
import com.ssafy.gallery.auth.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.gallery.auth.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import com.ssafy.gallery.auth.user.model.User;
import com.ssafy.gallery.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final LoginTokenRepository loginTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;


    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    // 추가
    public HashMap<String, String> login(OauthServerType oauthServerType, String authCode) {
        String secretKey = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        secretKey = secretKey.replace("-", "");
        System.out.println("secretKey: " + secretKey);

        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.getDomain())
                .orElseGet(() -> userRepository.save(user));

        String accessToken = JwtUtil.createJwt(secretKey, accessExpiration);
        LoginTokenDto accessTokenDto = new LoginTokenDto(accessToken, saved.getUserId(), accessExpiration);
        loginTokenRepository.save(accessTokenDto);

        String refreshToken = JwtUtil.createJwt(secretKey, refreshExpiration);
        LoginTokenDto refreshTokenDto = new LoginTokenDto(refreshToken, saved.getUserId(), refreshExpiration);
        loginTokenRepository.save(refreshTokenDto);

        HashMap<String, String> result = new HashMap<>();
        result.put("AccessToken", "Bearer " + accessToken);
        result.put("RefreshToken", refreshToken);

        return result;
    }
}