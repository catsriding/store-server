package com.catsriding.store.application.product.exception;

public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException() {
    }

    public ProductUnavailableException(String message) {
        super(message);
    }

    public ProductUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
