package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductOptionType;
import java.time.LocalDateTime;

public record ProductOptionWithValue(
        Long optionId,
        Long productId,
        String name,
        ProductOptionType optionType,
        boolean usable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long optionValueId,
        String optionValueName,
        Integer optionValuePrice,
        boolean optionValueUsable,
        LocalDateTime optionValueCreatedAt,
        LocalDateTime optionValueUpdatedAt
) {

}
