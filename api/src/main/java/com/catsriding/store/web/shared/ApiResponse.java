package com.catsriding.store.web.shared;

import static com.catsriding.store.web.shared.ResponseType.FAILURE;
import static com.catsriding.store.web.shared.ResponseType.FORBIDDEN;
import static com.catsriding.store.web.shared.ResponseType.SUCCESS;
import static com.catsriding.store.web.shared.ResponseType.UNAUTHORIZED;

public record ApiResponse<T>(
        ResponseType status,
        T data,
        String message) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<T>(SUCCESS, data, message);
    }

    public static <T> ApiResponse<T> failure(T data, String message) {
        return new ApiResponse<T>(FAILURE, data, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<T>(UNAUTHORIZED, null, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<T>(FORBIDDEN, null, message);
    }
}
