package com.ssafy.gallery.oauth.client;

import com.ssafy.gallery.oauth.dto.KakaoMemberResponse;
import com.ssafy.gallery.oauth.dto.KakaoPayApproveResponse;
import com.ssafy.gallery.oauth.dto.KakaoPayReadyResponse;
import com.ssafy.gallery.oauth.dto.KakaoToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.*;

public interface KakaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken fetchToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);

    @PostExchange(url = "https://open-api.kakaopay.com/online/v1/payment/ready", contentType = APPLICATION_JSON_VALUE + ";charset=UTF-8")
    KakaoPayReadyResponse fetchPayReady(@RequestHeader(name = AUTHORIZATION) String secretKey, @RequestBody MultiValueMap<String, String> params);

    @PostExchange(url = "https://open-api.kakaopay.com/online/v1/payment/approve", contentType = APPLICATION_JSON_VALUE)
    KakaoPayApproveResponse fetchPayApprove(@RequestHeader(name = AUTHORIZATION) String secretKey, @RequestParam MultiValueMap<String, String> params);
}