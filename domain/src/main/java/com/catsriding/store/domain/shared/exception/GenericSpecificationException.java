package com.catsriding.store.domain.shared.exception;

public class GenericSpecificationException extends RuntimeException {

    public GenericSpecificationException() {
    }

    public GenericSpecificationException(String message) {
        super(message);
    }

    public GenericSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
