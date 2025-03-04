package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductRegistrationResult;
import java.time.LocalDateTime;

public record ProductRegistrationResponse(
        String productId,
        String sellerId,
        String name,
        String statusType,
        LocalDateTime createdAt
) {

    public static ProductRegistrationResponse from(ProductRegistrationResult result) {
        return new ProductRegistrationResponse(
                String.valueOf(result.id()),
                String.valueOf(result.sellerId()),
                result.name(),
                result.statusType(),
                result.createdAt()
        );
    }
}
