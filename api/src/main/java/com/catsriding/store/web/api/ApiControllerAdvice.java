package com.catsriding.store.web.api;

import static com.catsriding.store.web.shared.ApiResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.catsriding.store.infra.database.shared.DataNotFoundException;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.support.ApiExceptionLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleIllegalArgumentException", "", e, request);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(failure(null, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleInvalidRequestException", "Bad request", e, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, e.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleDataNotFoundException", "Data not found", e, request);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(failure(null, e.getMessage()));
    }

}
