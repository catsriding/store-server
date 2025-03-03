package com.catsriding.store.web.api.auth.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("unitTest")
class UserLoginRequestTest {

    private static final String EMAIL_REGEX_PATTERN = "^(?!.*\\.\\.)[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";

    @ParameterizedTest
    @MethodSource("validEmails")
    @DisplayName("pass valid email patterns")
    void shouldPassValidEmailPatterns(String email) throws Exception {

        // When
        boolean isValid = Pattern.matches(EMAIL_REGEX_PATTERN, email);

        // Then
        assertTrue(isValid, "유효한 이메일이 거부됨: " + email);
    }

    static Stream<Arguments> validEmails() {
        return Stream.of(
                arguments("user@example.com"),
                arguments("user.name@domain.com"),
                arguments("user_name@sub.domain.net"),
                arguments("user+tag@gmail.com"),
                arguments("user123@example.co.uk"),
                arguments("valid-email@domain.io"),
                arguments("name@org.org"),
                arguments("user@domain.xyz")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    @DisplayName("fail invalid email patterns")
    void shouldFailInvalidEmailPatterns(String email) throws Exception {

        // When
        boolean isValid = Pattern.matches(EMAIL_REGEX_PATTERN, email);

        // Then
        assertFalse(isValid, "잘못된 이메일이 통과됨: " + email);
    }


    static Stream<Arguments> invalidEmails() {
        return Stream.of(
                arguments("user@domain"),           // ❌ 최상위 도메인 없음
                arguments("@domain.com"),           // ❌ 사용자명 없음
                arguments("user@.com"),             // ❌ 도메인명 없음
                arguments("user@domain..com"),      // ❌ 연속된 점
                arguments("user@@domain.com"),      // ❌ `@` 두 개
                arguments("user@domain,com"),       // ❌ `,` 포함
                arguments("user domain.com"),       // ❌ 공백 포함
                arguments("user@domain.c"),         // ❌ 최상위 도메인 한 글자
                arguments("user@.co.uk"),           // ❌ 도메인 없이 최상위 도메인만 존재
                arguments("user@domain..com")       // ❌ 연속된 점
        );
    }

}