package com.ssafy.gallery.user.controller;

import com.ssafy.gallery.oauth.type.OauthServerType;
import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/login/{oauthServerType}")
    ResponseEntity<ApiResponse<?>> login(
            HttpServletResponse response,
            @PathVariable OauthServerType oauthServerType,
            @RequestBody Map<String, Object> params
    ) {
        log.info("{} 로그인 시도, authCode: {}", oauthServerType, params.get("authCode"));
        boolean login = userService.login(response, oauthServerType, (String) params.get("authCode"));
        log.info("{} 로그인 결과: {}", oauthServerType, login);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(login));
    }

    @GetMapping("/logout")
    ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        log.info("로그아웃 요청");
        boolean result = userService.logout(request);
        log.info("로그아웃 결과: {}", result);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(result));
    }
}