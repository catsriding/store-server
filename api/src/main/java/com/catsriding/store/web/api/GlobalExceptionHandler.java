package com.catsriding.store.web.api;

import static com.catsriding.store.web.shared.ApiResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.catsriding.store.web.support.ApiExceptionLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleIllegalArgumentException", "", e, request);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(failure(null, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(
            HttpMessageConversionException e,
            HttpServletRequest request
    ) {
        ApiExceptionLogger.logWarning("handleHttpMessageConversionException", "Bad request", e, request);

        String errorMessage = "요청한 형식이 올바르지 않습니다. 입력값을 다시 확인해주세요.";
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, errorMessage));
    }

}
