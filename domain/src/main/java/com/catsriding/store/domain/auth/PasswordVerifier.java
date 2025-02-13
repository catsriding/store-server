package com.catsriding.store.domain.auth;

public interface PasswordVerifier {

    boolean verify(String rawPassword, String hashedPassword);

}
