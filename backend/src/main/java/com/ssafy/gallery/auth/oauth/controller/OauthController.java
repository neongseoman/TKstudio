package com.ssafy.gallery.auth.oauth.controller;

import com.ssafy.gallery.auth.oauth.OauthServerType;
import com.ssafy.gallery.auth.oauth.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class OauthController {
    private static final Logger logger = LogManager.getLogger(OauthController.class);

    private final OauthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        logger.info("로그인 페이지 : " + oauthServerType + response);
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    // 추가
    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<Long> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
        logger.info("로그인: " + code);
        Long login = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(login);
    }
}