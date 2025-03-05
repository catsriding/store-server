package com.catsriding.store.web.api.auth.response;

import com.catsriding.store.application.auth.model.LoginResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 데이터")
public record LoginResponse(
        @Schema(description = "토큰 유형", example = "Bearer")
        String tokenType,
        @Schema(description = "액세스 토큰", example = "Bearer eyJhbGciOi.WKERO.dsfpowkp...")
        String accessToken,
        @Schema(description = "토큰 만료 일시", example = "1756720438873")
        Long expiresIn
) {

    public static LoginResponse from(LoginResult loginResult) {
        return new LoginResponse(loginResult.tokenType(), loginResult.accessToken(), loginResult.expiresIn());
    }
}
