package com.ssafy.gallery.user.service;

import com.ssafy.gallery.auth.jwt.util.JwtUtil;
import com.ssafy.gallery.auth.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import com.ssafy.gallery.user.model.User;
import com.ssafy.gallery.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final LoginTokenRepository loginTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public boolean login(HttpServletResponse response, OauthServerType oauthServerType, String authCode) {
        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.getDomain())
                .orElseGet(() -> userRepository.save(user));

        String accessToken = jwtUtil.createToken("access");
        String refreshToken = jwtUtil.createToken("refresh");
        jwtUtil.saveTokens(accessToken, refreshToken, saved.getUserId());

        response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);
        return true;
    }

    public boolean logout(HttpServletRequest request) {
        String refreshToken = (String) request.getAttribute("refreshToken");

        if (refreshToken != null) {
            log.info("refreshToken: {}", refreshToken);

            if (loginTokenRepository.findById(refreshToken).isPresent()) {
                LoginTokenDto loginTokenDto = loginTokenRepository.findById(refreshToken).get();
                log.info("로그인한 유저의 정보: {}", loginTokenDto);

                String accessToken = loginTokenDto.getAccessToken();
                if (accessToken != null) {
                    if (loginTokenRepository.findById(accessToken).isPresent()) {
                        loginTokenRepository.deleteById(accessToken);
                    }
                    loginTokenRepository.deleteById(refreshToken);
                    return true;
                }
            }
        }
        return false;
    }
}