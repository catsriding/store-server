package com.catsriding.store.web.api.auth;

import static com.catsriding.store.web.shared.ApiSuccessResponse.success;

import com.catsriding.store.application.auth.AuthService;
import com.catsriding.store.application.auth.result.LoginResult;
import com.catsriding.store.web.api.auth.request.LoginRequest;
import com.catsriding.store.web.api.auth.response.LoginResponse;
import com.catsriding.store.web.shared.ApiErrorResponse;
import com.catsriding.store.web.shared.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "사용자 인증 관련 API")
@Slf4j
@RestController
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService authService) {
        this.service = authService;
    }

    @Operation(
            summary = "로그인 API",
            description = """
                          사용자가 아이디와 비밀번호를 입력하여 인증을 시도합니다.
                          - **아이디**: 이메일 형식
                          - **비밀번호**: 최소 4자 이상, 영문, 숫자, 특수기호 `@$!%*?&` 만 사용 가능
                          - **인증 성공**: JWT 액세스 토큰을 발급하며, 이후 API 호출 시 `Authorization` 헤더에 포함하여 사용
                          - **인증 실패**: 아이디 또는 비밀번호가 올바르지 않거나 계정이 정지 또는 비활성화된 경우
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                         description = "로그인 실패",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<LoginResponse>> loginApi(
            @RequestBody LoginRequest request
    ) {
        LoginResult loginResult = service.login(request.toCommand());
        LoginResponse response = LoginResponse.from(loginResult);

        return ResponseEntity
                .ok(success(response, "로그인이 성공적으로 완료되었습니다."));
    }

}
