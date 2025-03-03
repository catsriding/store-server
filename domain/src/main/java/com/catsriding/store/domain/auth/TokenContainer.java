package com.catsriding.store.domain.auth;

public record TokenContainer(
        String tokenType,
        String accessToken,
        Long expiresIn
) {

}