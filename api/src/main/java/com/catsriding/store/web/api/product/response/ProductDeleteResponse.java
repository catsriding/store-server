package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductDeleteResult;
import java.time.LocalDateTime;

public record ProductDeleteResponse(
        String productId,
        String sellerId,
        String name,
        String statusType,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    public static ProductDeleteResponse from(ProductDeleteResult result) {
        return new ProductDeleteResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.statusType(),
                result.updatedAt(),
                result.createdAt()
        );
    }
}
