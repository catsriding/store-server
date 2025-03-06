package com.catsriding.store.web.api.auth.request;

import com.catsriding.store.application.auth.model.UserLogin;
import com.catsriding.store.web.shared.InvalidRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Schema(description = "로그인 요청 데이터")
@Slf4j
public record LoginRequest(
        @Schema(description = "사용자 아이디: 이메일", example = "user@gmail.com")
        String username,
        @Schema(description = "사용자 비밀번호: 4자 이상, 영문/숫자/특수기호만 가능", example = "1234")
        String password
) {

    private static final String EMAIL_REGEX_PATTERN = "^(?!.*\\.\\.)[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_REGEX_PATTERN = "^[A-Za-z\\d@$!%*?&]{4,}$";

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
        if (!Pattern.matches(PASSWORD_REGEX_PATTERN, password)) {
            log.info("validate: Invalid password format - password={}", password);
            throw new InvalidRequestException("비밀번호는 4자 이상이어야 하며, 영문과 숫자, 그리고 특수기호 @$!%*?& 만 사용할 수 있습니다.");
        }
    }

    public UserLogin toCommand() {
        return new UserLogin(username, password);
    }
}
