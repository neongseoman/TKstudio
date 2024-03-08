package com.ssafy.gallery.auth.jwt.exception;

import com.ssafy.gallery.common.exception.ExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionEnum implements ExceptionEnum {
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "401", "토큰이 만료되었습니다"),
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_EXIST(HttpStatus.NOT_FOUND, "404", "토큰이 존재하지 않습니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}