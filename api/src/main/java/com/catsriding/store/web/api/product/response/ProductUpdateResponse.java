package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductUpdateResult;
import java.time.LocalDateTime;

public record ProductUpdateResponse(
        String productId,
        String sellerId,
        String name,
        String status,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    public static ProductUpdateResponse from(ProductUpdateResult result) {
        return new ProductUpdateResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.status(),
                result.updatedAt(),
                result.createdAt()
        );
    }
}
