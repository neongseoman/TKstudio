package com.ssafy.gallery.user.controller;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/login/{oauthServerType}")
    ResponseEntity<ApiResponse<?>> login(
            @PathVariable OauthServerType oauthServerType,
            @RequestBody Map<String, Object> params
    ) {
        log.info("authCode: {}", params.get("authCode"));
        HashMap<String, String> login = userService.login(oauthServerType, (String) params.get("authCode"));
        log.info("{} 로그인 성공: {}", oauthServerType, login);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(login));
    }
}