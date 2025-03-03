package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductDetailsResult;
import java.time.LocalDateTime;

public record ProductDetailsResponse(
        String productId,
        String sellerId,
        String name,
        String description,
        Integer price,
        Integer deliveryFee,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductDetailsResponse from(ProductDetailsResult result) {
        return new ProductDetailsResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.description(),
                result.price(),
                result.deliveryFee(),
                result.status(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
