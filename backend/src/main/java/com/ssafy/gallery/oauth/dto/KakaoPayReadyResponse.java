package com.ssafy.gallery.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.type.DateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoPayReadyResponse(
        String tid,
        String nextRedirectAppUrl,
        String nextRedirectMobileUrl,
        String nextRedirectPcUrl,
        String androidAppScheme,
        String iosAppScheme,
        DateTime createdAt
) {
}



