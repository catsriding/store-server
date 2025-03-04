package com.catsriding.store.web.shared;

import com.catsriding.store.domain.user.UserRoleType;

public record LoginUser(
        Long id,
        String username,
        UserRoleType roleType
) {

}
