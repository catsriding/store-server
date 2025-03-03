package com.catsriding.store.domain.auth;

public record TokenClaims(
        Long id,
        String username,
        String role
) {

    public static TokenClaims from(Long id, String username, String role) {
        return new TokenClaims(id, username, role);
    }
}
