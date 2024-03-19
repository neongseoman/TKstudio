package com.ssafy.gallery.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pay.kakao")
public record KakaoPayConfig(
        String cid,
        String secretKey
) {
}