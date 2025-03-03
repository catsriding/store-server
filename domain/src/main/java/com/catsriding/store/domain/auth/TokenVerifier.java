package com.catsriding.store.domain.auth;

public interface TokenVerifier {

    TokenClaims verify(String accessToken);

}
