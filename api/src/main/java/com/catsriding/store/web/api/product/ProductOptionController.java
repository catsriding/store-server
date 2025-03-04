package com.catsriding.store.web.api.product;

import com.catsriding.store.application.product.ProductOptionService;
import com.catsriding.store.application.product.result.ProductOptionDeleteResult;
import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.web.api.product.request.ProductOptionDeleteRequest;
import com.catsriding.store.web.api.product.request.ProductOptionRequest;
import com.catsriding.store.web.api.product.request.ProductOptionsRequest;
import com.catsriding.store.web.api.product.response.ProductOptionDeleteResponse;
import com.catsriding.store.web.api.product.response.ProductOptionResponse;
import com.catsriding.store.web.api.product.response.ProductOptionsResponse;
import com.catsriding.store.web.security.model.CurrentUser;
import com.catsriding.store.web.shared.ApiResponse;
import com.catsriding.store.web.shared.LoginUser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productOptionsApi(
            @PathVariable Long productId,
            @CurrentUser LoginUser user
    ) {
        ProductOptionsRequest request = ProductOptionsRequest.from(productId, user);
        List<ProductOptionWithValue> results = service.retrieveOptions(request.toCond());
        ProductOptionsResponse response = ProductOptionsResponse.from(results);
        return ResponseEntity
                .ok(ApiResponse.success(response, "상품의 옵션 목록을 성공적으로 조회했습니다."));
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

    @DeleteMapping("/{optionId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> productOptionDeleteApi(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @CurrentUser LoginUser user
    ) {
        ProductOptionDeleteRequest request = ProductOptionDeleteRequest.from(productId, optionId, user);
        ProductOptionDeleteResult result = service.deleteProductOption(request.toCommand());
        ProductOptionDeleteResponse response = ProductOptionDeleteResponse.from(result);
        return ResponseEntity
                .ok(ApiResponse.success(response, "상품 옵션이 성공적으로 삭제되었습니다."));
    }
}
