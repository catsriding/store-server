package com.catsriding.store.web.api.auth;

import static com.catsriding.store.web.shared.ApiSuccessResponse.success;

import com.catsriding.store.application.auth.AuthService;
import com.catsriding.store.application.auth.model.LoginResult;
import com.catsriding.store.web.api.auth.request.LoginRequest;
import com.catsriding.store.web.api.auth.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService authService) {
        this.service = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginApi(
            @RequestBody LoginRequest request
    ) {
        LoginResult loginResult = service.login(request.toCommand());
        LoginResponse response = LoginResponse.from(loginResult);

        return ResponseEntity
                .ok(success(response, "로그인이 성공적으로 완료되었습니다."));
    }

}
