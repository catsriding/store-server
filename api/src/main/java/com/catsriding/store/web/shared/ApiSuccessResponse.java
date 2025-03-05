package com.catsriding.store.web.shared;

import static com.catsriding.store.web.shared.ResponseType.SUCCESS;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API Success Response")
public record ApiSuccessResponse<T>(
        @Schema(description = "응답 처리 결과 코드", example = "SUCCESS")
        ResponseType status,
        @Schema(description = "응답 데이터")
        T data,
        @Schema(description = "응답 처리 결과 메시지", example = "요청을 성공적으로 처리하였습니다.")
        String message) {

    public static <T> ApiSuccessResponse<T> success(T data, String message) {
        return new ApiSuccessResponse<T>(SUCCESS, data, message);
    }
}
