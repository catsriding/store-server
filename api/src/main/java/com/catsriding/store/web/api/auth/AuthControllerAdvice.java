package com.catsriding.store.web.api.auth;

import static com.catsriding.store.web.shared.ApiErrorResponse.failure;

import com.catsriding.store.application.auth.exception.LoginFailureException;
import com.catsriding.store.web.support.ApiExceptionLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<?> handleLoginFailure(LoginFailureException e, HttpServletRequest request) {
        ApiExceptionLogger.logWarning("handleLoginFailure", "Login attempt failed", e, request);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(failure(null, e.getMessage()));
    }

}
