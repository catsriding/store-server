package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductOptionDeleteResult;
import java.time.LocalDateTime;

public record ProductOptionDeleteResponse(
        String optionId,
        String productId,
        String name,
        LocalDateTime updatedAt
) {

    public static ProductOptionDeleteResponse from(ProductOptionDeleteResult result) {
        return new ProductOptionDeleteResponse(
                String.valueOf(result.optionId()),
                String.valueOf(result.productId()),
                result.name(),
                result.updatedAt()
        );
    }
}
