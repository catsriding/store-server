package com.catsriding.store.infra.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String issuer,
        String secretKey,
        long expiresAccessToken,
        long expiresRefreshToken) {

}
