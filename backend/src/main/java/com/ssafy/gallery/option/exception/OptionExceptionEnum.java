package com.ssafy.gallery.option.exception;

import com.ssafy.gallery.common.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OptionExceptionEnum implements ExceptionEnum {
    ALREADY_PURCHASED(HttpStatus.BAD_REQUEST, 400, "이미 구매한 옵션입니다."),
    NO_OPTION(HttpStatus.BAD_REQUEST, 400, "존재하지 않는 옵션입니다."),
    PAY_CANCEL(HttpStatus.BAD_REQUEST, 400, "결제가 취소되었습니다."),
    PAY_FAIL(HttpStatus.BAD_REQUEST, 400, "결제가 실패했습니다."),
    NO_TID(HttpStatus.BAD_REQUEST, 400, "결제 정보가 없습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}
