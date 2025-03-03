package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.Product;
import java.time.LocalDateTime;

public record ProductDetailsResult(
        Long id,
        Long sellerId,
        String name,
        String description,
        Integer price,
        Integer deliveryFee,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductDetailsResult from(Product product) {
        return new ProductDetailsResult(
                product.id(),
                product.sellerId().id(),
                product.name(),
                product.description(),
                product.price(),
                product.deliveryFee(),
                product.status().name(),
                product.updatedAt(),
                product.createdAt()
        );
    }
}
