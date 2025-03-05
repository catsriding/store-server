package com.catsriding.store.web.api.product;

import static com.catsriding.store.web.shared.ApiSuccessResponse.success;

import com.catsriding.store.application.product.ProductOptionService;
import com.catsriding.store.application.product.result.ProductOptionDeleteResult;
import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.web.api.product.request.ProductOptionCreateRequest;
import com.catsriding.store.web.api.product.request.ProductOptionDeleteRequest;
import com.catsriding.store.web.api.product.request.ProductOptionUpdateRequest;
import com.catsriding.store.web.api.product.request.ProductOptionsRequest;
import com.catsriding.store.web.api.product.response.ProductOptionDeleteResponse;
import com.catsriding.store.web.api.product.response.ProductOptionResponse;
import com.catsriding.store.web.api.product.response.ProductOptionsResponse;
import com.catsriding.store.web.security.model.CurrentUser;
import com.catsriding.store.web.shared.ApiErrorResponse;
import com.catsriding.store.web.shared.ApiSuccessResponse;
import com.catsriding.store.web.shared.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Option Managements", description = "상품 옵션 관리 API")
@Slf4j
@RequestMapping(value = "/products/{productId}/options")
@RestController
public class ProductOptionController {

    private final ProductOptionService service;

    public ProductOptionController(ProductOptionService service) {
        this.service = service;
    }

    @Operation(
            summary = "상품 옵션 목록 조회 API",
            description = """
                          판매자가 특정 상품의 옵션 목록을 조회합니다.
                          - **권한**: `ROLE_USER`
                          - **상품 ID**: 옵션 목록을 조회할 상품의 고유 ID
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션 목록 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 또는 잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductOptionsResponse>> productOptionsApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductOptionsRequest request = ProductOptionsRequest.from(productId, user);
        List<ProductOptionWithValue> results = service.retrieveOptions(request.toCond());
        ProductOptionsResponse response = ProductOptionsResponse.from(results);
        return ResponseEntity
                .ok(success(response, "상품의 옵션 목록을 성공적으로 조회했습니다."));
    }

    @Operation(
            summary = "상품 옵션 등록 API",
            description = """
                          판매자가 특정 상품에 새로운 옵션을 추가합니다.
                          - **권한**: `ROLE_USER`
                          - **등록 가능 개수**: 최대 `3개`
                          - **옵션명**: 최대 25자
                          - **옵션 값 이름**: 최대 30자
                          - **옵션 타입**: `SELECT` | `INPUT`
                          - **옵션 사용 가능 여부**: `true` | `false`
                          - **옵션 값 목록**: `SELECT` 타입의 경우 최소 1개 이상 필수 | `INPUT` 타입의 경우 옵션 값 포함 불가
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션 등록 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 또는 잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductOptionResponse>> productOptionCreateApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @RequestBody ProductOptionCreateRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductOptionResult result = service.createProductOption(request.toCommand(productId, user));
        ProductOptionResponse response = ProductOptionResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품 옵션이 성공적으로 생성되었습니다."));
    }

    @Operation(
            summary = "상품 옵션 수정 API",
            description = """
                          판매자가 특정 상품의 옵션을 수정합니다.
                          - **권한**: `ROLE_USER`
                          - **옵션명**: 최대 25자
                          - **옵션 값 이름**: 최대 30자
                          - **옵션 타입**: `SELECT` | `INPUT`
                          - **사용 가능 여부**: `true` | `false`
                          - **옵션 값 목록**: `SELECT` 타입의 경우 최소 1개 이상 필수 | `INPUT` 타입은 옵션 값 포함 불가
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 옵션 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 옵션 또는 잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{optionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductOptionResponse>> productOptionUpdateApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @PathVariable @Parameter(description = "상품 옵션 ID") Long optionId,
            @RequestBody ProductOptionUpdateRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductOptionResult result = service.updateProductOption(request.toCommand(productId, optionId, user));
        ProductOptionResponse response = ProductOptionResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품 옵션이 성공적으로 수정되었습니다."));
    }

    @Operation(
            summary = "상품 옵션 삭제 API",
            description = """
                          판매자가 특정 상품의 옵션을 삭제합니다.
                          - **권한**: `ROLE_USER`
                          - **삭제 가능 여부**: 옵션이 존재하는 경우 삭제 가능
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 옵션 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 옵션 또는 잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{optionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductOptionDeleteResponse>> productOptionDeleteApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @PathVariable @Parameter(description = "상품 옵션 ID") Long optionId,
            @CurrentUser LoginUser user
    ) {
        ProductOptionDeleteRequest request = ProductOptionDeleteRequest.from(productId, optionId, user);
        ProductOptionDeleteResult result = service.deleteProductOption(request.toCommand());
        ProductOptionDeleteResponse response = ProductOptionDeleteResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품 옵션이 성공적으로 삭제되었습니다."));
    }
}
