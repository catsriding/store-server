package com.catsriding.store.domain.product;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class ProductOption {

    private final ProductOptionId id;
    private final ProductId productId;
    private final String name;
    private final ProductOptionType optionType;
    private final boolean usable;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<ProductOptionValue> optionValues;

    @Builder
    public ProductOption(
            ProductOptionId id,
            ProductId productId,
            String name,
            ProductOptionType optionType,
            boolean usable,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<ProductOptionValue> optionValues
    ) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.optionType = optionType;
        this.usable = usable;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.optionValues = optionValues;
    }

    public Long id() {
        return id.id();
    }

    public ProductOptionId optionId() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public String name() {
        return name;
    }

    public ProductOptionType optionType() {
        return optionType;
    }

    public boolean usable() {
        return usable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public List<ProductOptionValue> optionValues() {
        return optionValues;
    }
}
