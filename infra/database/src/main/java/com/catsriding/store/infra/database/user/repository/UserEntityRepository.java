package com.catsriding.store.infra.database.user.repository;

import com.catsriding.store.domain.user.User;
import com.catsriding.store.domain.user.repository.UserRepository;
import com.catsriding.store.infra.database.shared.DataNotFoundException;
import com.catsriding.store.infra.database.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class UserEntityRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserEntityRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = UserEntity.from(user);
        entity = userJpaRepository.save(entity);
        return entity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        return userJpaRepository.fetchByUsername(username)
                .map(UserEntity::toDomain)
                .orElseThrow(() -> {
                    log.warn("retrieveUserByUsername: User not found - username={}", username);
                    return new DataNotFoundException("요청한 사용자 정보를 찾을 수 없습니다. 입력한 아이디를 다시 확인해주세요.");
                });
    }
}
