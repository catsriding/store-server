package com.catsriding.store.web.api.product;

import static com.catsriding.store.web.shared.ApiSuccessResponse.success;

import com.catsriding.store.application.product.ProductService;
import com.catsriding.store.application.product.result.ProductDeleteResult;
import com.catsriding.store.application.product.result.ProductDetailsResult;
import com.catsriding.store.application.product.result.ProductRegisterResult;
import com.catsriding.store.application.product.result.ProductUpdateResult;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.domain.shared.PagedData;
import com.catsriding.store.web.api.product.request.ProductDeleteRequest;
import com.catsriding.store.web.api.product.request.ProductDetailsRequest;
import com.catsriding.store.web.api.product.request.ProductPagedRequest;
import com.catsriding.store.web.api.product.request.ProductRegisterRequest;
import com.catsriding.store.web.api.product.request.ProductUpdateRequest;
import com.catsriding.store.web.api.product.response.ProductDeleteResponse;
import com.catsriding.store.web.api.product.response.ProductDetailsResponse;
import com.catsriding.store.web.api.product.response.ProductRegisterResponse;
import com.catsriding.store.web.api.product.response.ProductUpdateResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Managements", description = "상품 관리 API")
@Slf4j
@RequestMapping(value = "/products")
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(
            summary = "상품 목록 조회 API",
            description = """
                          판매자가 등록한 상품 목록을 페이지네이션 방식으로 조회합니다.
                          - **권한**: `ROLE_USER`
                          - **페이지 번호**: 0부터 시작
                          - **페이지 크기**: 1이상
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                         description = "잘못된 요청 파라미터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<PagedData<ProductSummary>>> productPagedApi(
            @RequestParam @Parameter(description = "페이지 번호", example = "0") int pageNumber,
            @RequestParam @Parameter(description = "페이지 크기", example = "10") int pageSize,
            @CurrentUser LoginUser user
    ) {
        ProductPagedRequest request = ProductPagedRequest.from(pageNumber, pageSize, user);
        PagedData<ProductSummary> pagedData = service.retrievePagedProducts(request.toCond());
        return ResponseEntity
                .ok(success(pagedData, "상품 목록을 성공적으로 조회했습니다."));
    }

    @Operation(
            summary = "상품 등록 API",
            description = """
                          판매자가 새로운 상품을 등록합니다.
                          - **권한**: `ROLE_USER`
                          - **상품명**: 필수 입력
                          - **상품 설명**: 필수 입력
                          - **가격**: 최소 10원 이상
                          - **배송비**: 최소 0원 이상
                          - **상태**: `SALE` | `INACTIVE`
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductRegisterResponse>> productRegistrationApi(
            @RequestBody ProductRegisterRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductRegisterResult result = service.registerNewProduct(request.toCommand(user));
        ProductRegisterResponse response = ProductRegisterResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품이 성공적으로 등록되었습니다."));
    }

    @Operation(
            summary = "상품 정보 조회 API",
            description = """
                          판매자가 특정 상품의 상세 정보를 조회합니다.
                          - **권한**: `ROLE_USER`
                          - **상품 ID**: 조회할 상품의 고유 식별자
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "상품을 찾을 수 없는 경우",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductDetailsResponse>> productDetailsApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductDetailsRequest request = ProductDetailsRequest.from(productId, user);
        ProductDetailsResult result = service.retrieveProduct(request.toCond());
        ProductDetailsResponse response = ProductDetailsResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품 정보를 성공적으로 조회했습니다."));
    }

    @Operation(
            summary = "상품 수정 API",
            description = """
                          판매자가 기존 상품 정보를 수정합니다.
                          - **권한**: `ROLE_USER`
                          - **상품명**: 필수 입력
                          - **상품 설명**: 필수 입력
                          - **가격**: 최소 10원 이상
                          - **배송비**: 최소 0원 이상
                          - **상태**: `SALE` | `INACTIVE`
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductUpdateResponse>> productUpdateApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @RequestBody ProductUpdateRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductUpdateResult result = service.updateProduct(request.toCommand(productId, user));
        ProductUpdateResponse response = ProductUpdateResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품이 성공적으로 수정되었습니다."));
    }

    @Operation(
            summary = "상품 삭제 API",
            description = """
                          판매자가 상품을 삭제합니다.
                          - **권한**: `ROLE_USER`
                          - **상품 ID**: 삭제할 상품의 고유 ID
                          - **삭제 처리**: 상품 상태를 `DELETED`로 변경
                          """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                         content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<ApiSuccessResponse<ProductDeleteResponse>> productDeleteApi(
            @PathVariable @Parameter(description = "상품 ID") Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductDeleteRequest request = ProductDeleteRequest.from(productId, user);
        ProductDeleteResult result = service.deleteProduct(request.toCommand());
        ProductDeleteResponse response = ProductDeleteResponse.from(result);
        return ResponseEntity
                .ok(success(response, "상품이 성공적으로 삭제되었습니다."));
    }
}
