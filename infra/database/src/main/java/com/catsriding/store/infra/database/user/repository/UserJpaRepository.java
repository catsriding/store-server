package com.catsriding.store.infra.database.user.repository;

import com.catsriding.store.infra.database.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserJpaRepositoryExtension {

}
