package com.catsriding.store.web.shared;

import static com.catsriding.store.web.shared.ResponseType.FAILURE;
import static com.catsriding.store.web.shared.ResponseType.FORBIDDEN;
import static com.catsriding.store.web.shared.ResponseType.UNAUTHORIZED;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Error Response")
public record ApiErrorResponse<T>(
        @Schema(description = "응답 처리 결과 코드", example = "FAILURE")
        ResponseType status,
        @Schema(description = "응답 데이터")
        T data,
        @Schema(description = "응답 처리 결과 메시지", example = "요청한 형식이 올바르지 않습니다. 입력값을 다시 확인해주세요.")
        String message) {

    public static <T> ApiErrorResponse<T> failure(T data, String message) {
        return new ApiErrorResponse<T>(FAILURE, data, message);
    }

    public static <T> ApiErrorResponse<T> unauthorized(String message) {
        return new ApiErrorResponse<T>(UNAUTHORIZED, null, message);
    }

    public static <T> ApiErrorResponse<T> forbidden(String message) {
        return new ApiErrorResponse<T>(FORBIDDEN, null, message);
    }
}
