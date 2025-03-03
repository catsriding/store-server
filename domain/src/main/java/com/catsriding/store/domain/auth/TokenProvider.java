package com.catsriding.store.domain.auth;

public interface TokenProvider {

    TokenContainer issue(TokenClaims user, long currentTimeMillis1);

}
