package com.catsriding.store.web.shared;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, HttpStatus status) {
        super(message);
    }

}
