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
    @DisplayName("hash password")
    void shouldHashPassword() throws Exception {

        //  Given
        String rawPassword = "1234";

        //  When
        String hashedPassword = passwordEncoder.encode(rawPassword);

        //  Then
        log.info("shouldHashPassword: raw={}, hashed={}", rawPassword, hashedPassword);

    }

    @Test
    @DisplayName("match password")
    void shouldMatchPassword() throws Exception {

        //  Given
        String rawPassword = "1234";
        String hashedPassword = "{bcrypt}$2a$10$3HfIwlJhhPBjm2iJFE110e1xit5aSD4uZxWpz/YqsU5dBI4YSKNGK";

        //  When
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);

        //  Then
        Assertions.assertThat(matches).isTrue();

    }

}