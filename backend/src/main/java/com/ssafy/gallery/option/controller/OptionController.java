package com.ssafy.gallery.option.controller;

import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionCategory;
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
        int userId = (int) request.getAttribute("userId");
        log.info("{}유저 옵션리스트 요청", userId);
        List<OptionBuyLog> buyOptionList = optionService.getBuyOptionList(userId);
        List<OptionStore> optionList = optionService.getList();
        for (OptionBuyLog o : buyOptionList) {
            optionList.get(o.getOptionId() - 1).setPurchased(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(optionList));
    }

    @GetMapping("/category")
    ResponseEntity<ApiResponse<?>> optionCategory() {
        List<OptionCategory> category = optionService.getCategory();
        log.info("옵션 카테고리 요청: {}", category);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(category));
    }

    @PostMapping("/buy")
    ResponseEntity<ApiResponse<?>> optionBuy(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params
    ) {
        int userId = (int) request.getAttribute("userId");
        int optionId = (int) params.get("optionId");
        log.info("{}회원이 {}옵션 구매", userId, optionId);
        optionService.buyOption(userId, optionId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }
}