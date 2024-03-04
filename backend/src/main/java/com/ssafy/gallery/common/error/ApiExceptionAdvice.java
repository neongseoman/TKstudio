package com.ssafy.gallery.common.error;

import com.ssafy.gallery.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus
    public ResponseEntity<ApiResponse<?>> handleApiException(HttpServletRequest request, ApiException e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.builder()
                        .status(String.valueOf(e.getStatus()))
                        .message(e.getCode())
                        .data(null)
                        .build());
    }
}