package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.Product;
import java.time.LocalDateTime;

public record ProductUpdateResult(
        Long id,
        Long sellerId,
        String name,
        String statusType,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    public static ProductUpdateResult from(Product product) {
        return new ProductUpdateResult(
                product.id(),
                product.sellerId().id(),
                product.name(),
                product.status().name(),
                product.updatedAt(),
                product.createdAt());
    }

}
