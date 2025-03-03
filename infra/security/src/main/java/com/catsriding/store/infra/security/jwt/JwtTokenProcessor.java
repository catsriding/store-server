package com.catsriding.store.infra.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.catsriding.store.domain.auth.TokenClaims;
import com.catsriding.store.domain.auth.TokenContainer;
import com.catsriding.store.domain.auth.TokenProvider;
import com.catsriding.store.domain.auth.TokenVerifier;
import com.catsriding.store.infra.security.exception.TokenValidationException;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProcessor implements TokenProvider, TokenVerifier {

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

    @Override
    public TokenClaims verify(String accessToken) {
        try {
            String jwt = extractJwt(accessToken);
            Map<String, Claim> claims = JWT.require(algorithm())
                    .build()
                    .verify(jwt)
                    .getClaims();
            Long id = Long.parseLong(claims.get("sub").asString());
            String username = claims.get("username").asString();
            String role = claims.get("role").asString();
            return TokenClaims.from(id, username, role);
        } catch (TokenExpiredException e) {
            log.info("Token verification failed: Expired token detected - reason={}", e.getMessage());
            throw new TokenValidationException("세션이 만료되었습니다. 다시 로그인해 주세요.", e);
        } catch (AlgorithmMismatchException e) {
            log.info("Token verification failed: Token signature algorithm mismatch - reason={}", e.getMessage());
            throw new TokenValidationException("인증 정보 검증 중 문제가 발생했습니다. 다시 로그인해 주세요.", e);
        } catch (SignatureVerificationException e) {
            log.info("Token verification failed: Invalid token signature detected - reason={}", e.getMessage());
            throw new TokenValidationException("유효하지 않은 인증 정보입니다. 다시 로그인해 주세요.", e);
        } catch (JWTVerificationException e) {
            log.info("Token verification failed: General JWT verification error - reason={}", e.getMessage());
            throw new TokenValidationException("인증 정보가 유효하지 않습니다. 다시 로그인해 주세요.", e);
        } catch (Exception e) {
            log.info("Token verification failed: Invalid token - reason={}", e.getMessage());
            throw new TokenValidationException("인증 정보가 유효하지 않습니다. 다시 로그인해 주세요.", e);
        }
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
