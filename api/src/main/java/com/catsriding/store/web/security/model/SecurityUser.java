package com.catsriding.store.web.security.model;

import com.catsriding.store.application.auth.model.AuthenticatedUser;
import com.catsriding.store.web.shared.LoginUser;
import com.catsriding.store.domain.user.UserRoleType;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {

    private final LoginUser loginUser;

    public SecurityUser(
            AuthenticatedUser authenticatedUser
    ) {
        super(authenticatedUser.username(), "{noop}pwd", authorities(authenticatedUser.roleType()));
        this.loginUser = new LoginUser(authenticatedUser.id(), authenticatedUser.username(), authenticatedUser.roleType());
    }

    private static List<GrantedAuthority> authorities(UserRoleType role) {
        return role.permissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public LoginUser loginUser() {
        return loginUser;
    }

}
