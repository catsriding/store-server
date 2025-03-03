package com.catsriding.store.web.api.auth.request;

import com.catsriding.store.application.auth.model.UserLogin;
import com.catsriding.store.web.shared.InvalidRequestException;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public record LoginRequest(
        String username,
        String password
) {

    private static final String EMAIL_REGEX_PATTERN = "^(?!.*\\.\\.)[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";

    public LoginRequest {
        validate(username, password);
    }

    private static void validate(String username, String password) {
        if (!StringUtils.hasText(username)) {
            log.info("validate: Required username is missing or empty - username={}", username);
            throw new InvalidRequestException("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요.");
        }
        if (!StringUtils.hasText(password)) {
            log.info("validate: Required password is missing or empty - username={}", username);
            throw new InvalidRequestException("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요.");
        }
        if (!Pattern.matches(EMAIL_REGEX_PATTERN, username)) {
            log.info("validate: Invalid username format - username={}", username);
            throw new InvalidRequestException("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요." + username);
        }
    }

    public UserLogin toCommand() {
        return new UserLogin(username, password);
    }
}
