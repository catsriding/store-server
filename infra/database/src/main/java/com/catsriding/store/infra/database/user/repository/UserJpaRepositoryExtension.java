package com.catsriding.store.infra.database.user.repository;

import com.catsriding.store.infra.database.user.entity.UserEntity;
import java.util.Optional;

public interface UserJpaRepositoryExtension {

    Optional<UserEntity> fetchByUsername(String username);

}
