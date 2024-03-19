package com.ssafy.gallery.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.type.DateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record KakaoPayApproveResponse(
        String aid,
        String tid,
        String cid,
        String sid,
        String partnerOrderId,
        String partnerUserId,
        String paymentMethodType,
        Amount amount,
        CardInfo cardInfo,
        String itemName,
        String itemCode,
        Integer quantity,
        DateTime createdAt,
        DateTime approvedAt,
        String payload
) {
    @JsonNaming(SnakeCaseStrategy.class)
    public record Amount(
            Integer total,
            Integer taxFree,
            Integer vat,
            Integer point,
            Integer discount,
            Integer greenDeposit
    ) {
    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record CardInfo(
            String kakaopayPurchaseCorp,
            String kakaopayPurchaseCorpCode,
            String kakaopayIssuerCorp,
            String kakaopayIssuerCorpCode,
            String bin,
            String cardType,
            String installMonth,
            String approvedId,
            String cardMid,
            String interestFreeInstall,
            String installmentType,
            String cardItemCode
    ) {
    }
}
