package com.catsriding.store.infra.database.user.entity;

import com.catsriding.store.domain.user.User;
import com.catsriding.store.domain.user.UserId;
import com.catsriding.store.domain.user.UserRoleType;
import com.catsriding.store.domain.user.UserStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @Column(name = "status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusType statusType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserEntity from(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.id();
        entity.username = user.username();
        entity.password = user.password();
        entity.roleType = user.roleType();
        entity.statusType = user.statusType();
        entity.createdAt = user.createdAt();
        entity.updatedAt = user.updatedAt();
        return entity;
    }

    public User toDomain() {
        return User.builder()
                .id(UserId.withId(id()))
                .username(username)
                .password(password)
                .roleType(roleType)
                .statusType(statusType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public Long id() {
        return id;
    }

    public String username() {
        return username;
    }
}
