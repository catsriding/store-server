package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.Product;
import java.time.LocalDateTime;

public record ProductRegistrationResult(
        Long id,
        Long sellerId,
        String name,
        String statusType,
        LocalDateTime createdAt
) {

    public static ProductRegistrationResult from(Product product) {
        return new ProductRegistrationResult(
                product.id(),
                product.sellerId().id(),
                product.name(),
                product.status().name(),
                product.createdAt());
    }

}
