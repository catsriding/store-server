package com.catsriding.store.application.auth.exception;

public class LoginFailureException extends RuntimeException {

    public LoginFailureException() {
    }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
