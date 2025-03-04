package com.catsriding.store.application.auth.model;

import com.catsriding.store.domain.auth.TokenClaims;
import com.catsriding.store.domain.user.UserRoleType;

public record AuthenticatedUser(
        Long id,
        String username,
        UserRoleType roleType
) {

    public static AuthenticatedUser from(TokenClaims claims) {
        return new AuthenticatedUser(claims.id(), claims.username(), UserRoleType.of(claims.role()));
    }
}
