package com.catsriding.store.web.security.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

import com.catsriding.store.application.auth.AuthService;
import com.catsriding.store.application.auth.model.AuthenticatedUser;
import com.catsriding.store.infra.security.exception.TokenValidationException;
import com.catsriding.store.web.security.model.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    private static final Set<String> WHITELIST = Set.of(
            "/health",
            "/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (!isWhitelist(requestUri)) {
            String method = request.getMethod();
            String accessToken = request.getHeader(AUTHORIZATION);

            if (StringUtils.hasText(accessToken)) {
                try {
                    AuthenticatedUser authenticatedUser = authService.authenticate(accessToken);
                    SecurityUser securityUser = new SecurityUser(authenticatedUser);
                    Authentication authentication = createAuthentication(securityUser);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Authenticated request: method={}, uri={}, remoteIp={}, userAgent={}, userId={}",
                            method,
                            requestUri,
                            request.getRemoteAddr(),
                            request.getHeader(USER_AGENT),
                            authenticatedUser.id());
                } catch (TokenValidationException e) {
                    request.setAttribute("AUTH_ERROR_MESSAGE", e.getMessage());
                    throw new AuthenticationException("JWT Verification Failed") {
                    };
                }

            } else {
                log.info("Unauthenticated request: method={}, uri={}, remoteIp={}, userAgent={}",
                        method,
                        requestUri,
                        request.getRemoteAddr(),
                        request.getHeader(USER_AGENT));
            }
        }
        filterChain.doFilter(request, response);
    }

    private static boolean isWhitelist(String uri) {
        return WHITELIST.contains(uri);
    }

    private static Authentication createAuthentication(SecurityUser securityUser) {
        return new UsernamePasswordAuthenticationToken(
                securityUser,
                securityUser.getPassword(),
                securityUser.getAuthorities());
    }
}
