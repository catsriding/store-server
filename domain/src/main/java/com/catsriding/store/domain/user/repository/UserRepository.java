package com.catsriding.store.domain.user.repository;

import com.catsriding.store.domain.user.User;

public interface UserRepository {

    User save(User user);

    User loadUserByUsername(String username);

}
