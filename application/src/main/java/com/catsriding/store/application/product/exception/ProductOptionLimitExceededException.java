package com.catsriding.store.application.product.exception;

public class ProductOptionLimitExceededException extends RuntimeException {

    public ProductOptionLimitExceededException() {
    }

    public ProductOptionLimitExceededException(String message) {
        super(message);
    }

    public ProductOptionLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
