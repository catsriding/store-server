package com.catsriding.store.web.security.handler;

import static com.catsriding.store.web.shared.ApiErrorResponse.forbidden;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        String errorMessage = "해당 리소스에 접근할 수 있는 권한이 없습니다. 필요한 권한을 확인한 후 다시 시도해 주세요.";

        log.warn(
                "Authorization failure: Access denied due to insufficient permissions - method={}, uri={}, remoteIp={}, userAgent={}, user={}, reason={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                request.getHeader(USER_AGENT),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous",
                accessDeniedException.getMessage()
        );

        ApiErrorResponse<Object> errorResponse = forbidden(errorMessage);
        response.setStatus(SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
