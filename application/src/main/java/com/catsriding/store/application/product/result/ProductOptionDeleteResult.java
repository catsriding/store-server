package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.ProductOption;
import java.time.LocalDateTime;

public record ProductOptionDeleteResult(
        Long optionId,
        Long productId,
        String name,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    public static ProductOptionDeleteResult from(ProductOption productOption) {
        return new ProductOptionDeleteResult(
                productOption.id(),
                productOption.productId().id(),
                productOption.name(),
                productOption.updatedAt(),
                productOption.createdAt());
    }
}
