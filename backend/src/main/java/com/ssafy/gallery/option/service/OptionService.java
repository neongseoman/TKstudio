package com.ssafy.gallery.option.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gallery.option.model.OptionBuyLog;
import com.ssafy.gallery.option.model.OptionCategory;
import com.ssafy.gallery.option.model.OptionStore;
import com.ssafy.gallery.option.record.KakaoPayApproveResponse;
import com.ssafy.gallery.option.record.KakaoPayReadyResponse;
import com.ssafy.gallery.option.repository.OptionBuyLogRepository;
import com.ssafy.gallery.option.repository.OptionCategoryRepository;
import com.ssafy.gallery.option.repository.OptionStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OptionService {
    private final OptionStoreRepository optionStoreRepository;
    private final OptionBuyLogRepository optionBuyLogRepository;
    private final OptionCategoryRepository optionCategoryRepository;

    @Value("${pay.kakao.secret_key}")
    private String secretKey;

    @Cacheable(cacheNames = "optionList", cacheManager = "cacheManager")
    public List<OptionStore> getList() {
        return optionStoreRepository.findAll();
    }

    public Optional<OptionStore> getOption(int optionId) {
        return optionStoreRepository.findById(optionId);
    }

    @Cacheable(cacheNames = "optionCategory", cacheManager = "cacheManager")
    public List<OptionCategory> getCategory() {
        return optionCategoryRepository.findAll();
    }

    @Cacheable(cacheNames = "buyOption", key = "#userId", condition = "#userId != null", cacheManager = "cacheManager")
    public List<OptionBuyLog> getBuyOptionList(int userId) {
        return optionBuyLogRepository.findAllByUserId(userId);
    }

    @CacheEvict(cacheNames = "buyOption", key = "#userId", cacheManager = "cacheManager")
    public void buyOption(int userId, int optionId) {
        OptionBuyLog optionBuyLog = OptionBuyLog.builder().userId(userId).optionId(optionId).build();
        optionBuyLogRepository.save(optionBuyLog);
    }

    public KakaoPayReadyResponse paymentReady(int userId, String optionName, int optionId, int cost) throws Exception {
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        HttpEntity<?> urlRequest = new HttpEntity<>(mapToJson(getReadyParams(userId, optionName, optionId, cost)), getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, urlRequest, KakaoPayReadyResponse.class);
    }

    public KakaoPayApproveResponse paymentApprove(String tid, String pgToken) throws Exception {
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        HttpEntity<?> urlRequest = new HttpEntity<>(mapToJson(getApproveParams(tid, pgToken)), getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(url, urlRequest, KakaoPayApproveResponse.class);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        return headers;
    }

    private Map<String, Object> getReadyParams(int userId, String optionName, int optionId, int cost) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("partner_order_id", "partner_order_id");
        params.put("partner_user_id", "partner_user_id");
        params.put("item_name", optionName);
        params.put("item_code", optionId);
        params.put("quantity", "1");
        params.put("total_amount", String.valueOf(cost));
        params.put("tax_free_amount", "0");
        params.put("approval_url", "https://j10a101.p.ssafy.io/api/v1/option/payment/success?user_id=" + userId);
        params.put("cancel_url", "https://j10a101.p.ssafy.io/api/v1/option/payment/cancel");
        params.put("fail_url", "https://j10a101.p.ssafy.io/api/v1/option/payment/fail");
        return params;
    }

    private Map<String, Object> getApproveParams(String tid, String pgToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", tid);
        params.put("partner_order_id", "partner_order_id");
        params.put("partner_user_id", "partner_user_id");
        params.put("pg_token", pgToken);
        return params;
    }

    public String mapToJson(Map<String, Object> map) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

}
