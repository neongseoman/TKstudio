package com.ssafy.gallery.option.controller;

import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.service.OptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/buy")
    ResponseEntity<ApiResponse<?>> optionBuy(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params
    ) {
        int userId = (int) request.getAttribute("userId");
        int optionId = (int) params.get("optionId");
        log.info("{} 회원이 {} 옵션 구매", userId, optionId);
        optionService.buyOption(userId, optionId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }
}