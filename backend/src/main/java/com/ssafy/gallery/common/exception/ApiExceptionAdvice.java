package com.ssafy.gallery.common.exception;

import com.ssafy.gallery.common.response.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ssafy.gallery.common.exception.CommonExceptionEnum.DATA_ACCESS_ERROR;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        System.out.println("this is control advice");
        return ResponseEntity
                .status(e.getStatus())
                .header("error","error")
                .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus
    public ResponseEntity<ApiResponse<?>> handleDataException() {
        return ResponseEntity
                .status(DATA_ACCESS_ERROR.getStatus())
                .body(ApiResponse.error(DATA_ACCESS_ERROR.getMessage()));
    }
}