package com.ssafy.gallery.option.controller;

import com.ssafy.gallery.common.exception.ApiExceptionFactory;
import com.ssafy.gallery.common.response.ApiResponse;
import com.ssafy.gallery.option.exception.OptionExceptionEnum;
import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionCategory;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.dto.KakaoPayApproveResponse;
import com.ssafy.gallery.option.dto.KakaoPayReadyResponse;
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
        log.info("{}유저 옵션리스트 요청", userId);
        List<OptionStore> optionList = optionService.getList();
        HashMap<Integer, OptionStore> result = new HashMap<>();
        for (OptionStore o : optionList) {
            log.info("옵션: {}", o);
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
        log.info("옵션 카테고리 요청: {}", category);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(category));
    }

    @PostMapping("/payment/ready")
    ResponseEntity<ApiResponse<?>> paymentReadyReq(
            HttpServletRequest request,
            @RequestBody Map<String, Object> params
    ) throws Exception {
        int userId = (int) request.getAttribute("userId");
        int optionId = (int) params.get("optionId");
        log.info("{}회원이 {}옵션 구매 요청", userId, optionId);

        List<OptionBuyLog> buyOptionList = optionService.getBuyOptionList(userId);
        for (OptionBuyLog o : buyOptionList) {
            // 이미 구매한 옵션 예외처리
            if (o.getOptionId() == optionId) {
                log.info("이미 구매한 옵션입니다: {}", o);
                throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.ALREADY_PURCHASED);
            }
        }

        Optional<OptionStore> option = optionService.getOption(optionId);
        if (option.isEmpty()) {
            log.info("존재하지 않는 옵션 구매 요청입니다");
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.NO_OPTION);
        }

        String optionName = option.get().getOptionName();
        int cost = option.get().getCost();
        log.info("옵션이름:{}, 가격:{}", optionName, cost);

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
        log.info("결제 성공: {}, {}",userId, pgToken);

        Optional<KakaoPayReadyDto> kakaoPayReadyDto = kakaoPayReadyRepository.findById(userId);
        if (kakaoPayReadyDto.isEmpty()) {
            log.info("결제 정보가 없습니다.");
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.NO_TID);
        }

        String tid = kakaoPayReadyDto.get().getTid();
        KakaoPayApproveResponse kakaoPayApproveResponse = optionService.paymentApprove(tid, pgToken);
        log.info("결제 결과: {}", kakaoPayApproveResponse);

        if(kakaoPayApproveResponse == null){
            log.info("결제가 실패했습니다.");
            throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_FAIL);
        }

        log.info("옵션 구매 처리");
        optionService.buyOption(Integer.parseInt(userId), Integer.parseInt(kakaoPayApproveResponse.itemCode()));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(true));
    }

    @GetMapping("/payment/cancel")
    public void cancel() {
        log.info("결제가 취소되었습니다.");
        throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_CANCEL);
    }

    @GetMapping("/payment/fail")
    public void fail() {
        log.info("결제가 실패했습니다.");
        throw ApiExceptionFactory.fromExceptionEnum(OptionExceptionEnum.PAY_FAIL);
    }
}