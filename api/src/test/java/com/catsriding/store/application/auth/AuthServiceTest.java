package com.catsriding.store.application.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.catsriding.store.application.auth.exception.LoginFailureException;
import com.catsriding.store.application.auth.model.UserLogin;
import com.catsriding.store.application.auth.result.LoginResult;
import com.catsriding.store.domain.auth.PasswordVerifier;
import com.catsriding.store.domain.auth.TokenContainer;
import com.catsriding.store.domain.auth.TokenProvider;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.User;
import com.catsriding.store.domain.user.UserId;
import com.catsriding.store.domain.user.UserRoleType;
import com.catsriding.store.domain.user.UserStatusType;
import com.catsriding.store.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unitTest")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordVerifier passwordVerifier;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private ClockHolder clockHolder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("✅ 정상 로그인")
    void shouldReturnTokenWhenLoginSuccess() {

        // Given
        User user = mockUser(UserStatusType.ACTIVE);
        UserLogin command = new UserLogin(user.username(), "validPassword");
        when(userRepository.loadUserByUsername(user.username())).thenReturn(user);
        when(passwordVerifier.verify("validPassword", user.password())).thenReturn(true);
        when(tokenProvider.issue(any(), anyLong())).thenReturn(mockTokenContainer());

        // When
        LoginResult result = authService.login(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.accessToken()).isEqualTo("mockedAccessToken");
    }

    @Test
    @DisplayName("🚫 패스워드 불일치시 예외 발생")
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        User user = mockUser(UserStatusType.ACTIVE);
        UserLogin command = new UserLogin(user.username(), "wrongPassword");
        when(userRepository.loadUserByUsername(user.username())).thenReturn(user);
        when(passwordVerifier.verify("wrongPassword", user.password())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요.");
    }

    @Test
    @DisplayName("🚫 비활성화된 계정 예외 발생")
    void shouldThrowExceptionWhenUserIsInactive() {
        // Given
        User user = mockUser(UserStatusType.INACTIVE);
        UserLogin command = new UserLogin(user.username(), "validPassword");
        when(userRepository.loadUserByUsername(user.username())).thenReturn(user);
        when(passwordVerifier.verify("validPassword", user.password())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("이 계정은 현재 비활성화되어 있어 로그인할 수 없습니다. 관리자에게 문의해 주세요.");
    }

    @Test
    @DisplayName("🚫 정지된 계정 예외 발생")
    void shouldThrowExceptionWhenUserIsSuspended() {
        // Given
        User user = mockUser(UserStatusType.SUSPENDED);
        UserLogin command = new UserLogin(user.username(), "validPassword");
        when(userRepository.loadUserByUsername(user.username())).thenReturn(user);
        when(passwordVerifier.verify("validPassword", user.password())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("이 계정은 일시적으로 정지되었습니다. 관리자에게 문의해 주세요.");
    }

    @Test
    @DisplayName("🚫 삭제된 계정 예외 발생")
    void shouldThrowExceptionWhenUserIsDeleted() {
        // Given
        User user = mockUser(UserStatusType.DELETED);
        UserLogin command = new UserLogin(user.username(), "validPassword");
        when(userRepository.loadUserByUsername(user.username())).thenReturn(user);
        when(passwordVerifier.verify("validPassword", user.password())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("이 계정은 삭제된 상태입니다. 새로운 계정을 생성하거나 관리자에게 문의해 주세요.");
    }

    @Test
    @DisplayName("🚫 존재하지 않는 사용자 예외 발생")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Given
        UserLogin command = new UserLogin("nonexistent@user.com", "password");
        when(userRepository.loadUserByUsername("nonexistent@user.com")).thenThrow(new LoginFailureException(
                "아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요."));

        // When & Then
        assertThatThrownBy(() -> authService.login(command))
                .isInstanceOf(LoginFailureException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다. 다시 확인 후 시도해주세요.");
    }

    private User mockUser(UserStatusType statusType) {
        return User.builder()
                .id(UserId.withoutId())
                .username("user@gmail.com")
                .password("hashedPassword")
                .roleType(UserRoleType.ROLE_USER)
                .statusType(statusType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private TokenContainer mockTokenContainer() {
        return new TokenContainer("Bearer", "mockedAccessToken", 3600L);
    }

}