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
    private static final String PASSWORD_REGEX_PATTERN = "^[A-Za-z\\d@$!%*?&]{4,}$";

    @ParameterizedTest
    @MethodSource("validEmails")
    @DisplayName("✅ 유효한 이메일 형식이면 검증 통과")
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
    @DisplayName("🚫 잘못된 이메일 형식이면 검증 실패")
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

    @ParameterizedTest
    @MethodSource("validPasswords")
    @DisplayName("✅ 유효한 비밀번호 형식이면 검증 통과")
    void shouldPassValidPasswordPatterns(String password) throws Exception {
        // When
        boolean isValid = Pattern.matches(PASSWORD_REGEX_PATTERN, password);

        // Then
        assertTrue(isValid, "유효한 비밀번호가 거부됨: " + password);
    }

    static Stream<Arguments> validPasswords() {
        return Stream.of(
                arguments("abcd"),       // ✅ 최소 길이 (4자)
                arguments("1234"),       // ✅ 숫자만
                arguments("Abcd1234"),   // ✅ 대소문자 및 숫자 조합
                arguments("@$!%*?&"),    // ✅ 허용된 특수기호만 포함
                arguments("A1@!"),       // ✅ 혼합된 문자 (대문자, 숫자, 특수기호)
                arguments("longpassword123!@"),  // ✅ 길이가 길어도 유효
                arguments("Aa1!Aa1!")    // ✅ 패턴에 맞는 다양한 조합
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    @DisplayName("🚫 잘못된 비밀번호 형식이면 검증 실패")
    void shouldFailInvalidPasswordPatterns(String password) throws Exception {
        // When
        boolean isValid = Pattern.matches(PASSWORD_REGEX_PATTERN, password);

        // Then
        assertFalse(isValid, "잘못된 비밀번호가 통과됨: " + password);
    }

    static Stream<Arguments> invalidPasswords() {
        return Stream.of(
                arguments("abc"),        // ❌ 3자 이하 (최소 4자 이상)
                arguments("12"),         // ❌ 3자 이하 (최소 4자 이상)
                arguments("abcd!@#$%^"), // ❌ 허용되지 않은 특수기호 포함
                arguments("abcd 1234"),  // ❌ 공백 포함
                arguments("한글비번"),     // ❌ 한글만
                arguments("한글비번1234"), // ❌ 한글 포함
                arguments("123 "),       // ❌ 공백 포함
                arguments(" "),          // ❌ 빈 문자열 (공백 포함)
                arguments("password<>"), // ❌ 허용되지 않은 특수기호 `<`, `>`
                arguments("PassWord한글") // ❌ 한글 포함
        );
    }
}