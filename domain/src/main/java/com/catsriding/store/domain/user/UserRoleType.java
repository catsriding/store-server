package com.catsriding.store.domain.user;

import java.util.Arrays;
import java.util.List;

public enum UserRoleType {

    ROLE_DEV(List.of("ROLE_DEV", "ROLE_ADMIN", "ROLE_USER", "ROLE_GUEST")),
    ROLE_ADMIN(List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_GUEST")),
    ROLE_USER(List.of("ROLE_USER", "ROLE_GUEST")),
    ROLE_GUEST(List.of("ROLE_GUEST"));

    private final List<String> permissions;

    UserRoleType(List<String> permissions) {
        this.permissions = permissions;
    }

    public static UserRoleType of(String role) {
        return Arrays.stream(UserRoleType.values())
                .filter(userRole -> userRole.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid user role: " + role));
    }

    public List<String> permissions() {
        return permissions;
    }
}
