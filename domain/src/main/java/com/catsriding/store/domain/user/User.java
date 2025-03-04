package com.catsriding.store.domain.user;

import com.catsriding.store.domain.auth.PasswordVerifier;
import java.time.LocalDateTime;
import lombok.Builder;

public class User {

    private final UserId id;
    private final String username;
    private final String password;
    private final UserRoleType roleType;
    private final UserStatusType statusType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    private User(
            UserId id,
            String username,
            String password,
            UserRoleType roleType,
            UserStatusType statusType,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleType = roleType;
        this.statusType = statusType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() {
        return id.id();
    }

    public UserId userId() {
        return id;
    }

    public String username() {
        return username;
    }

    public UserRoleType roleType() {
        return roleType;
    }

    public UserStatusType statusType() {
        return statusType;
    }

    public String password() {
        return password;
    }

    public boolean verifyPassword(String rawPassword, PasswordVerifier verifier) {
        return verifier.verify(rawPassword, password);
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }
}
