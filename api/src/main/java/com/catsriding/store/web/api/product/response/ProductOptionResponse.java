package com.catsriding.store.web.api.product.response;

import com.catsriding.store.application.product.result.ProductOptionResult;
import com.catsriding.store.application.product.result.ProductOptionResult.ProductOptionValueResult;
import java.time.LocalDateTime;
import java.util.List;

public record ProductOptionResponse(
        String optionId,
        String productId,
        String name,
        String optionType,
        boolean usable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OptionValue> optionValues
) {

    public static ProductOptionResponse from(ProductOptionResult result) {
        return new ProductOptionResponse(
                String.valueOf(result.id()),
                String.valueOf(result.productId()),
                result.name(),
                result.optionType(),
                result.usable(),
                result.createdAt(),
                result.updatedAt(),
                result.optionValueResults().stream()
                        .map(OptionValue::from)
                        .toList()
        );
    }

    private record OptionValue(
            String optionValueId,
            String name,
            Integer price,
            boolean usable,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        private static OptionValue from(ProductOptionValueResult result) {
            return new OptionValue(
                    String.valueOf(result.id()),
                    result.name(),
                    result.price(),
                    result.usable(),
                    result.createdAt(),
                    result.updatedAt()
            );
        }
    }
}
