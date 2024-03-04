package com.ssafy.gallery.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionEnum {

    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
