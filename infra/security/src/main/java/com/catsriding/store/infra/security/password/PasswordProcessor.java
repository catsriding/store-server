package com.catsriding.store.infra.security.password;

import com.catsriding.store.domain.auth.PasswordVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordProcessor implements PasswordVerifier {

    private final PasswordEncoder passwordEncoder;

    public PasswordProcessor(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
