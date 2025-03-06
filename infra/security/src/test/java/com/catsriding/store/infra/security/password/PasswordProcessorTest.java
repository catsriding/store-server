package com.catsriding.store.infra.security.password;

import com.catsriding.store.infra.security.SecurityContextTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Tag("unitTest")
@Slf4j
class PasswordProcessorTest extends SecurityContextTest {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("✅ 비밀번호 해싱에 성공 시 bcrypt 타입 추가 및 길이 60 이상으로 변환")
    void hashedPasswordShouldIncludeBcryptPrefixAndBeAtLeast60CharactersLong() {
        // Given
        String rawPassword = "1234";

        // When
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Then
        log.info("hashedPasswordShouldIncludeBcryptPrefixAndBeAtLeast60CharactersLong: raw={}, hashed={}",
                rawPassword,
                hashedPassword);
        Assertions.assertThat(hashedPassword).startsWith("{bcrypt}$2a$"); // bcrypt 접두어 확인
        Assertions.assertThat(hashedPassword.length()).isGreaterThanOrEqualTo(60); // bcrypt 기본 길이 확인
    }

    @Test
    @DisplayName("✅ 비밀번호 해싱 결과 - 원본과 다른 값이어야 함")
    void hashedPasswordShouldBeDifferentFromRaw() {
        // Given
        String rawPassword = "1234";

        // When
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Then
        log.info("hashedPasswordShouldBeDifferentFromRaw: raw={}, hashed={}", rawPassword, hashedPassword);
        Assertions.assertThat(hashedPassword).isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("✅ 비밀번호 검증 시 해싱된 값과 일치하면 인증 성공")
    void validPasswordShouldMatchHashedPassword() {
        // Given
        String rawPassword = "1234";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // When
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);

        // Then
        log.info("validPasswordShouldMatchHashedPassword: raw={}, hashed={}, matches={}",
                rawPassword,
                hashedPassword,
                matches);
        Assertions.assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("🚫 비밀번호 검증 시 해싱된 값과 일치하지 않으면 인증 실패")
    void invalidPasswordShouldNotMatchHashedPassword() {
        // Given
        String rawPassword = "1234";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        String wrongPassword = "wrongPassword";

        // When
        boolean matches = passwordEncoder.matches(wrongPassword, hashedPassword);

        // Then
        log.info("invalidPasswordShouldNotMatchHashedPassword: wrongPassword={}, hashed={}, matches={}",
                wrongPassword,
                hashedPassword,
                matches);
        Assertions.assertThat(matches).isFalse();
    }
}