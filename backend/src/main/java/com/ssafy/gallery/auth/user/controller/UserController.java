package com.ssafy.gallery.auth.user.controller;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        log.info("로그인 페이지 : " + oauthServerType + response);
        String redirectUrl = userService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    // 추가
    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<HashMap<String, String>> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestParam("code") String code, HttpServletResponse response
    ) {
        HashMap<String, String> login = userService.login(oauthServerType, code);
        log.info("{} 로그인 성공: {}", oauthServerType, login);
        return ResponseEntity.ok(login);
    }
}