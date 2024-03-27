package com.ssafy.gallery.option.controller;

import com.ssafy.gallery.common.exception.ApiExceptionFactory;
import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.option.dto.KakaoPayApproveResponse;
import com.ssafy.gallery.option.dto.KakaoPayReadyResponse;
import com.ssafy.gallery.option.exception.OptionExceptionEnum;
import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionCategory;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.service.OptionService;
import com.ssafy.gallery.redis.dto.KakaoPayReadyDto;
import com.ssafy.gallery.redis.repository.KakaoPayReadyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/option")
@RestController
public class OptionController {
    private final OptionService optionService;
    private final KakaoPayReadyRepository kakaoPayReadyRepository;

    @GetMapping("/list")
    ResponseEntity<ApiResponse<?>> optionList(HttpServletRequest request) {
        int userId = (int) request.getAttribute("userId");
        List<OptionStore> optionList = optionService.getList();
        HashMap<Integer, OptionStore> result = new HashMap<>();
        for (OptionStore o : optionList) {
            result.put(o.getOptionId(), o);
        }

        List<OptionBuyLog> buyOptionList = optionService.getBuyOptionList(userId);
        for (OptionBuyLog o : buyOptionList) {
            result.get(o.getOptionId()).setPurchased(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(result.values()));
    }

    @GetMapping("/category")
    ResponseEntity<ApiResponse<?>> optionCategory() {
        List<OptionCategory> category = optionService.getCategory();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(category));
    }

    @PostMapping("/payment/ready")
    ResponseEntity<ApiResponse<?>> paymentReadyReq(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
        int userId = (int) request.getAttribute("userId");
        int optionId = (int) params.get("optionId");

        // 이미 구매한 옵션 예외처리
        List<OptionBuyLog> buyOptionList = optionService.getBuyOptionList(userId);
        for (OptionBuyLog o : buyOptionList) {
            if (o.getOptionId() == optionId) {
                throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.ALREADY_PURCHASED);
            }
        }

        // 존재하지 않는 옵션 예외처리
        Optional<OptionStore> option = optionService.getOption(optionId);
        if (option.isEmpty()) {
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.NO_OPTION);
        }

        String optionName = option.get().getOptionName();
        int cost = option.get().getCost();
        KakaoPayReadyResponse kakaoPayReadyResponse = optionService.paymentReady(userId, optionName, optionId, cost);
        KakaoPayReadyDto kakaoPayReadyDto = KakaoPayReadyDto.builder().id(userId).tid(kakaoPayReadyResponse.tid()).build();
        kakaoPayReadyRepository.save(kakaoPayReadyDto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(kakaoPayReadyResponse.nextRedirectMobileUrl()));
    }

    @GetMapping("/payment/success")
    ResponseEntity<ApiResponse<?>> paymentApproveReq(
            @RequestParam("user_id") String userId,
            @RequestParam("pg_token") String pgToken
    ) throws Exception {
        Optional<KakaoPayReadyDto> kakaoPayReadyDto = kakaoPayReadyRepository.findById(userId);
        if (kakaoPayReadyDto.isEmpty()) {
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.NO_TID);
        }

        String tid = kakaoPayReadyDto.get().getTid();
        KakaoPayApproveResponse kakaoPayApproveResponse = optionService.paymentApprove(tid, pgToken);

        if (kakaoPayApproveResponse == null) {
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_FAIL);
        }

        optionService.buyOption(Integer.parseInt(userId), Integer.parseInt(kakaoPayApproveResponse.itemCode()));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    @GetMapping("/payment/cancel")
    public void cancel() {
        throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_CANCEL);
    }

    @GetMapping("/payment/fail")
    public void fail() {
        throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_FAIL);
    }
}