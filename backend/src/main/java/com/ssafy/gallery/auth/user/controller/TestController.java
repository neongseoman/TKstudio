package com.ssafy.gallery.auth.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/test")
    ResponseEntity<String> login(

    ) {

        return ResponseEntity.ok("테스트 성공!");
    }
}
