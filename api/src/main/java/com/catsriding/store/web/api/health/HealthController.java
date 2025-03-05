package com.catsriding.store.web.api.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "API Server Health Check", description = "API 서버 상태를 확인하는 엔드포인트")
@Slf4j
@RestController
public class HealthController {

    @Operation(
            summary = "헬스 체크 API",
            description = """
                          API 서버의 상태를 확인하는 엔드포인트입니다.
                          - **정상 동작**: `200 OK` 응답을 반환
                          - **사용 예시**: 배포 후 서버 상태 확인, 로드 밸런서 상태 체크 등
                          """
    )
    @ApiResponse(responseCode = "200", description = "서버 정상 동작")
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
