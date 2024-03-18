package com.ssafy.gallery.option.controller;

import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.service.OptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/option")
@RestController
public class OptionController {
    private final OptionService optionService;

    @GetMapping("/list")
    ResponseEntity<ApiResponse<?>> optionList(HttpServletRequest request) {
        log.info("옵션리스트 요청");
        List<OptionStore> optionList = optionService.getList();
        log.info("옵션리스트: {}", optionList);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(optionList));
    }
}