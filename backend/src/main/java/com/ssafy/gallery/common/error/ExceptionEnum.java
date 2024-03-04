package com.ssafy.gallery.common.error;

import org.springframework.http.HttpStatus;

public interface ExceptionEnum {

    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
