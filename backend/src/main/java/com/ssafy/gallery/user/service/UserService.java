package com.ssafy.gallery.user.service;

import com.ssafy.gallery.auth.jwt.util.JwtUtil;
import com.ssafy.gallery.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.oauth.type.OauthServerType;
import com.ssafy.gallery.redis.dto.LoginTokenDto;
import com.ssafy.gallery.redis.repository.LoginTokenRepository;
import com.ssafy.gallery.user.model.LoginLog;
import com.ssafy.gallery.user.model.User;
import com.ssafy.gallery.user.repository.LoginLogRepository;
import com.ssafy.gallery.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final LoginTokenRepository loginTokenRepository;
    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepository;
    private final JwtUtil jwtUtil;

    public boolean login(HttpServletRequest request, HttpServletResponse response, OauthServerType oauthServerType, String authCode) {
        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.getDomain())
                .orElseGet(() -> userRepository.save(user));

        String accessToken = jwtUtil.createToken("access");
        String refreshToken = jwtUtil.createToken("refresh");
        jwtUtil.saveTokens(accessToken, refreshToken, saved.getUserId());

        LoginLog loginLog = LoginLog.builder().userId(user.getUserId()).loginIp(getClientIP(request)).build();
        loginLogRepository.save(loginLog);

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

    private static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.info("> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info(">  WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            log.info("> getRemoteAddr : " + ip);
        }
        log.info("> Result : IP Address : " + ip);

        return ip;
    }
}