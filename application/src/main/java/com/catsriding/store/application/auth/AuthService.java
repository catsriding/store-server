package com.catsriding.store.application.auth;

import static com.catsriding.store.domain.user.UserStatusType.ACTIVE;

import com.catsriding.store.application.auth.model.UserLogin;
import com.catsriding.store.application.auth.exception.LoginFailureException;
import com.catsriding.store.application.auth.model.AuthenticatedUser;
import com.catsriding.store.application.auth.model.LoginResult;
import com.catsriding.store.domain.auth.PasswordVerifier;
import com.catsriding.store.domain.auth.TokenClaims;
import com.catsriding.store.domain.auth.TokenContainer;
import com.catsriding.store.domain.auth.TokenProvider;
import com.catsriding.store.domain.auth.TokenVerifier;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordVerifier passwordVerifier;
    private final TokenProvider tokenProvider;
    private final TokenVerifier tokenVerifier;
    private final ClockHolder clockHolder;

    public AuthService(
            UserRepository userRepository,
            PasswordVerifier passwordVerifier,
            TokenProvider tokenProvider,
            TokenVerifier tokenVerifier,
            ClockHolder clockHolder
    ) {
        this.userRepository = userRepository;
        this.passwordVerifier = passwordVerifier;
        this.tokenProvider = tokenProvider;
        this.tokenVerifier = tokenVerifier;
        this.clockHolder = clockHolder;
    }

    public LoginResult login(UserLogin command) {
        User user = userRepository.retrieveUserByUsername(command.username());
        verifyPassword(user, command.password());
        verifyUserStatus(user);

        TokenClaims tokenClaims = TokenClaims.from(user.id(), user.username(), user.role().name());
        TokenContainer tokenContainer = tokenProvider.issue(tokenClaims, clockHolder.currentTimeMillis());

        log.info("login: Successfully logged in - userId={}, username={}", user.id(), user.username());
        return LoginResult.from(tokenContainer);
    }

    private void verifyPassword(User user, String rawPassword) {
        if (user.verifyPassword(rawPassword, passwordVerifier)) return;

        log.info("verifyPassword: User password does not match - username={}", user.username());
        throw new LoginFailureException("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요.");
    }

    private void verifyUserStatus(User user) {
        if (user.status() == ACTIVE) return;

        log.warn("verifyUserStatus: User account is not active - username={}, status={}",
                user.username(),
                user.status());

        String message = switch (user.status()) {
            case INACTIVE -> "이 계정은 현재 비활성화되어 있어 로그인할 수 없습니다. 관리자에게 문의해 주세요.";
            case SUSPENDED -> "이 계정은 일시적으로 정지되었습니다. 관리자에게 문의해 주세요.";
            case DELETED -> "이 계정은 삭제된 상태입니다. 새로운 계정을 생성하거나 관리자에게 문의해 주세요.";
            default -> "현재 계정 상태로는 로그인할 수 없습니다. 자세한 사항은 관리자에게 문의해 주세요.";
        };

        throw new LoginFailureException(message);
    }

    public AuthenticatedUser authenticate(String accessToken) {
        TokenClaims tokenClaims = tokenVerifier.verify(accessToken);
        AuthenticatedUser authenticatedUser = AuthenticatedUser.from(tokenClaims);

        log.info("authenticate: Successfully authenticated user - userId={}, username={}",
                authenticatedUser.id(),
                authenticatedUser.username());

        return authenticatedUser;
    }
}
