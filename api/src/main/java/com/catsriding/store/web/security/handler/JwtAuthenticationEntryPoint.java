package com.catsriding.store.web.security.handler;

import static com.catsriding.store.web.shared.ApiErrorResponse.unauthorized;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.catsriding.store.web.shared.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        String errorMessage = (String) request.getAttribute("AUTH_ERROR_MESSAGE");
        if (errorMessage == null || errorMessage.isBlank()) errorMessage = "인증이 필요합니다. 로그인 후 다시 시도해 주세요.";

        log.warn(
                "Authentication required: Access denied due to missing authentication - method={}, uri={}, remoteIp={}, userAgent={}, reason={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                request.getHeader(USER_AGENT),
                authException.getMessage()
        );

        ApiErrorResponse<Object> errorResponse = unauthorized(errorMessage);
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}