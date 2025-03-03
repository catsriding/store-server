package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.Product;
import java.time.LocalDateTime;

public record ProductDeleteResult(
        Long id,
        Long sellerId,
        String name,
        String status,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    public static ProductDeleteResult from(Product product) {
        return new ProductDeleteResult(
                product.id(),
                product.sellerId().id(),
                product.name(),
                product.status().name(),
                product.updatedAt(),
                product.createdAt());
    }

}
