package com.catsriding.store.web.api;

import static com.catsriding.store.web.shared.ApiErrorResponse.failure;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.catsriding.store.infra.database.shared.DataNotFoundException;
import com.catsriding.store.web.shared.InvalidRequestException;
import com.catsriding.store.web.support.ApiExceptionLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandler {

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
