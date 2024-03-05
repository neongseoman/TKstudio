package com.ssafy.gallery.user.controller;


import com.ssafy.gallery.example.controller.ExampleApiController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "유저", description = "유저 API")
@Log4j2
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(ExampleApiController.class);

    @GetMapping("/login/kakao")
    public String kakaoLogin(
            @RequestParam String state,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String error_description) {
        logger.info("카카오 로그인 시도");

        // 성공
        if (code != null) {
            System.out.println("code: " + code);
        }

        // 실패
        if (error != null) {
            System.out.println("error: " + error);
        }
        if (error_description != null) {
            System.out.println("error_description: " + error_description);
        }

        // 여기서 받은 파라미터를 이용한 로직을 수행하면 됩니다.

        // 반환값은 적절한 뷰 이름 또는 리다이렉션 경로일 수 있습니다.
        return "login";
    }



}
