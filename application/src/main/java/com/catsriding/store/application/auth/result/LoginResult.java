package com.catsriding.store.application.auth.result;

import com.catsriding.store.domain.auth.TokenContainer;

public record LoginResult(
        String tokenType,
        String accessToken,
        Long expiresIn
) {

    public static LoginResult from(TokenContainer tokenContainer) {
        return new LoginResult(tokenContainer.tokenType(), tokenContainer.accessToken(), tokenContainer.expiresIn());
    }
}
