package com.catsriding.store.infra.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.catsriding.store.domain.auth.TokenClaims;
import com.catsriding.store.domain.auth.TokenContainer;
import com.catsriding.store.domain.auth.TokenProvider;
import com.catsriding.store.domain.auth.TokenVerifier;
import com.catsriding.store.infra.security.exception.TokenValidationException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProcessor implements TokenProvider {

    private static final String BEARER = "bearer";

    private final JwtProperties jwtProperties;

    public JwtTokenProcessor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public TokenContainer issue(TokenClaims claims, long currentTimeMillis) {
        Date expiresIn = expiresAt(currentTimeMillis, jwtProperties.expiresAccessToken());
        Algorithm algorithm = algorithm();

        String jwt = JWT.create()
                .withSubject(claims.id().toString())
                .withClaim("username", claims.username())
                .withClaim("role", claims.role())
                .withIssuer(jwtProperties.issuer())
                .withIssuedAt(issuedAt(currentTimeMillis))
                .withExpiresAt(expiresIn)
                .sign(algorithm);
        String accessToken = prefixAccessToken(jwt);
        return new TokenContainer(BEARER, accessToken, expiresIn.getTime());
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(jwtProperties.secretKey());
    }

    private Date issuedAt(long currentTimeMillis) {
        return new Date(currentTimeMillis);
    }

    private Date expiresAt(long currentTimeMillis, long expires) {
        return new Date(currentTimeMillis + expires);
    }

    private static String prefixAccessToken(String jwt) {
        return String.format("%s %s", BEARER, jwt);
    }

    private String extractJwt(String accessToken) {
        return accessToken.substring(BEARER.length() + 1);
    }
}
