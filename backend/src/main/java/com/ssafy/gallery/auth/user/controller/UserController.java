package com.ssafy.gallery.auth.user.controller;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.user.service.UserService;
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
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        logger.info("로그인 페이지 : " + oauthServerType + response);
        String redirectUrl = userService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    // 추가
    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<Integer> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code
    ) {
        logger.info("로그인: " + code);
        int login = userService.login(oauthServerType, code);
        return ResponseEntity.ok(login);
    }
}