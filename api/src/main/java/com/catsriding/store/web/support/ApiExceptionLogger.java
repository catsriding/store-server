package com.catsriding.store.web.support;

import static org.springframework.http.HttpHeaders.USER_AGENT;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiExceptionLogger {

    public static void logWarning(String handler, String message, Exception e, HttpServletRequest request) {
        log.warn(
                """
                {}: {} - method={}, uri={}, remoteIp={}, userAgent={}, user={}, reason={}
                """,
                handler,
                message,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                request.getHeader(USER_AGENT),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous",
                e.getMessage()
        );
    }

}
