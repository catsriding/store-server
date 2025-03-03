package com.catsriding.store.web.api.auth.response;

import com.catsriding.store.application.auth.model.LoginResult;

public record LoginResponse(
        String tokenType,
        String accessToken,
        Long expiresIn
) {

    public static LoginResponse from(LoginResult loginResult) {
        return new LoginResponse(loginResult.tokenType(), loginResult.accessToken(), loginResult.expiresIn());
    }
}
