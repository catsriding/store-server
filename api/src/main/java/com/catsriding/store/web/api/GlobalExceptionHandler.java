package com.catsriding.store.web.api;

import static com.catsriding.store.web.shared.ApiErrorResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
                .status(BAD_REQUEST)
                .body(failure(null, "요청이 올바르지 않습니다. 요청 정보를 확인하고 다시 시도해주세요."));
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

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleNullPointerException", "", e, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, "요청 데이터에 누락된 값이 있습니다. 필수 값을 확인하고 다시 시도해주세요."));
    }

}
