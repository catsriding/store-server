package com.catsriding.store.infra.security.exception;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException() {
    }

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
