package com.catsriding.store.infra.database.user.repository;

import static com.catsriding.store.infra.database.user.entity.QUserEntity.userEntity;

import com.catsriding.store.infra.database.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserJpaRepositoryImpl implements UserJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserEntity> fetchByUsername(String username) {
        return Optional.ofNullable(
                queryFactory
                        .select(userEntity)
                        .from(userEntity)
                        .where(userEntity.username.eq(username))
                        .fetchOne()
        );
    }
}
