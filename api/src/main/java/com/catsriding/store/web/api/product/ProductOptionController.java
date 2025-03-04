package com.catsriding.store.web.api.product;

import com.catsriding.store.application.product.ProductOptionService;
import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.web.api.product.request.ProductOptionRequest;
import com.catsriding.store.web.api.product.response.ProductOptionResponse;
import com.catsriding.store.web.security.model.CurrentUser;
import com.catsriding.store.web.shared.ApiResponse;
import com.catsriding.store.web.shared.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/products/{productId}/options")
@RestController
public class ProductOptionController {

    private final ProductOptionService service;

    public ProductOptionController(ProductOptionService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productOptionCreateApi(
            @PathVariable Long productId,
            @RequestBody ProductOptionRequest request,
            @CurrentUser LoginUser user
    ) {
        ProductOptionResult result = service.createProductOption(request.toCommand(productId, user));
        ProductOptionResponse response = ProductOptionResponse.from(result);
        return ResponseEntity
                .ok(ApiResponse.success(response, "상품 옵션이 성공적으로 생성되었습니다."));
    }
}
