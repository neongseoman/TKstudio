package com.ssafy.gallery.option.exception;

import com.ssafy.gallery.common.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OptionExceptionEnum implements ExceptionEnum {
    ALREADY_PURCHASED(HttpStatus.BAD_REQUEST, 400, "이미 구매한 옵션입니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}
