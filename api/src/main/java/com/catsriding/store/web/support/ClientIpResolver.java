package com.catsriding.store.web.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientIpResolver {

    public String resolve(HttpServletRequest servletRequest) {
        String clientIp = servletRequest.getRemoteAddr();
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("X-Forwarded-For");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = servletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.hasText(clientIp)) {
            clientIp = "127.0.0.1";
        }
        return clientIp;
    }

}
