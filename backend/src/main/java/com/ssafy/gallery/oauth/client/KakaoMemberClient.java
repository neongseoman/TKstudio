package com.ssafy.gallery.oauth.client;

import com.ssafy.gallery.oauth.config.KakaoPayConfig;
import com.ssafy.gallery.oauth.dto.KakaoPayReadyResponse;
import com.ssafy.gallery.oauth.type.OauthServerType;
import com.ssafy.gallery.oauth.dto.KakaoMemberResponse;
import com.ssafy.gallery.oauth.dto.KakaoToken;
import com.ssafy.gallery.user.model.User;
import com.ssafy.gallery.oauth.config.KakaoOauthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final KakaoPayConfig kakaoPayConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.kakao;
    }

    @Override
    public User fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode)); // (1)
        KakaoMemberResponse kakaoMemberResponse =
                kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());  // (2)
        return kakaoMemberResponse.toDomain();  // (3)
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }

    public KakaoPayReadyResponse fetchReady(String itemName, String totalAmount) {
        log.info("secretKey: {}", kakaoPayConfig.secretKey());
        log.info("아이템: {}", readyRequestParams(itemName, totalAmount));

        KakaoPayReadyResponse payReady = kakaoApiClient.fetchPayReady(kakaoPayConfig.secretKey(), readyRequestParams(itemName, totalAmount));
        return payReady;
    }

    private MultiValueMap<String, String> readyRequestParams(String itemName, String totalAmount) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", kakaoPayConfig.cid());
        params.add("partner_order_id", "partner_order_id");
        params.add("partner_user_id", "partner_user_id");
        params.add("item_name", itemName);
        params.add("quantity", "1");
        params.add("total_amount", totalAmount);
        params.add("tax_free_amount", "0");
        params.add("approval_url", "https://j10a101.p.ssafy.io/option/ready/success");
        params.add("cancel_url", "https://j10a101.p.ssafy.io/option/ready/cancel");
        params.add("fail_url", "https://j10a101.p.ssafy.io/option/ready/fail");
        return params;
    }
}