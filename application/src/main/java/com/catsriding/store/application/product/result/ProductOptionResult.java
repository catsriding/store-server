package com.catsriding.store.application.product.result;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionValue;
import java.time.LocalDateTime;
import java.util.List;

public record ProductOptionResult(
        Long id,
        Long productId,
        String name,
        String optionType,
        boolean usable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ProductOptionValueResult> optionValueResults
) {

    public static ProductOptionResult from(ProductOption productOption) {
        return new ProductOptionResult(
                productOption.id(),
                productOption.productId().id(),
                productOption.name(),
                productOption.optionType().name(),
                productOption.usable(),
                productOption.createdAt(),
                productOption.updatedAt(),
                productOption.optionValues().stream()
                        .map(ProductOptionValueResult::from)
                        .toList()
        );
    }

    public record ProductOptionValueResult(
            Long id,
            String name,
            Integer price,
            boolean usable,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        public static ProductOptionValueResult from(ProductOptionValue optionValue) {
            return new ProductOptionValueResult(
                    optionValue.id(),
                    optionValue.name(),
                    optionValue.price(),
                    optionValue.usable(),
                    optionValue.createdAt(),
                    optionValue.updatedAt()
            );
        }
    }
}
