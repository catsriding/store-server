package com.catsriding.store.web.api.product;

import com.catsriding.store.application.product.ProductService;
import com.catsriding.store.application.product.result.ProductRegistrationResult;
import com.catsriding.store.application.product.result.ProductUpdateResult;
import com.catsriding.store.web.api.product.request.ProductRegistrationRequest;
import com.catsriding.store.web.api.product.request.ProductUpdateRequest;
import com.catsriding.store.web.api.product.response.ProductRegistrationResponse;
import com.catsriding.store.web.api.product.response.ProductUpdateResponse;
import com.catsriding.store.web.security.model.CurrentUser;
import com.catsriding.store.web.shared.ApiResponse;
import com.catsriding.store.web.shared.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/products")
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
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
                .ok(ApiResponse.success(response, "상품이 성공적으로 등록되었습니다."));
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
                .ok(ApiResponse.success(response, "상품이 성공적으로 수정되었습니다."));
    }
}
