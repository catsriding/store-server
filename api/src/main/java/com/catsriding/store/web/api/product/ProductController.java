package com.catsriding.store.web.api.product;

import com.catsriding.store.application.product.ProductService;
import com.catsriding.store.application.product.result.ProductDeleteResult;
import com.catsriding.store.application.product.result.ProductDetailsResult;
import com.catsriding.store.application.product.result.ProductRegistrationResult;
import com.catsriding.store.application.product.result.ProductUpdateResult;
import com.catsriding.store.domain.shared.PagedData;
import com.catsriding.store.web.api.product.request.ProductDeleteRequest;
import com.catsriding.store.web.api.product.request.ProductDetailsRequest;
import com.catsriding.store.web.api.product.request.ProductPagedRequest;
import com.catsriding.store.web.api.product.request.ProductRegistrationRequest;
import com.catsriding.store.web.api.product.request.ProductUpdateRequest;
import com.catsriding.store.web.api.product.response.ProductDeleteResponse;
import com.catsriding.store.web.api.product.response.ProductDetailsResponse;
import com.catsriding.store.web.api.product.response.ProductRegistrationResponse;
import com.catsriding.store.web.api.product.response.ProductUpdateResponse;
import com.catsriding.store.web.security.model.CurrentUser;
import com.catsriding.store.web.shared.ApiSuccessResponse;
import com.catsriding.store.web.shared.LoginUser;
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

@Slf4j
@RequestMapping(value = "/products")
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productPagedApi(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @CurrentUser LoginUser user
    ) {
        ProductPagedRequest request = ProductPagedRequest.from(pageNumber, pageSize, user);
        PagedData<?> pagedData = service.retrievePagedProducts(request.toCond());
        return ResponseEntity
                .ok(ApiSuccessResponse.success(pagedData, "상품 목록을 성공적으로 조회했습니다."));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productRegistrationApi(
            @RequestBody ProductRegistrationRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductRegistrationResult result = service.registerNewProduct(request.toCommand(user));
        ProductRegistrationResponse response = ProductRegistrationResponse.from(result);
        return ResponseEntity
                .ok(ApiSuccessResponse.success(response, "상품이 성공적으로 등록되었습니다."));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productDetailsApi(
            @PathVariable Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductDetailsRequest request = ProductDetailsRequest.from(productId, user);
        ProductDetailsResult result = service.retrieveProduct(request.toCond());
        ProductDetailsResponse response = ProductDetailsResponse.from(result);
        return ResponseEntity
                .ok(ApiSuccessResponse.success(response, "상품 정보를 성공적으로 조회했습니다."));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productUpdateApi(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductUpdateResult result = service.updateProduct(request.toCommand(productId, user));
        ProductUpdateResponse response = ProductUpdateResponse.from(result);
        return ResponseEntity
                .ok(ApiSuccessResponse.success(response, "상품이 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productDeleteApi(
            @PathVariable Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductDeleteRequest request = ProductDeleteRequest.from(productId, user);
        ProductDeleteResult result = service.deleteProduct(request.toCommand());
        ProductDeleteResponse response = ProductDeleteResponse.from(result);
        return ResponseEntity
                .ok(ApiSuccessResponse.success(response, "상품이 성공적으로 삭제되었습니다."));
    }
}
