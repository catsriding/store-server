package com.catsriding.store.domain.product;

import java.time.LocalDateTime;
import lombok.Builder;

public class ProductOptionValue {

    private final ProductOptionValueId id;
    private final ProductOptionId optionId;
    private final String name;
    private final Integer price;
    private final boolean usable;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductOptionValue(
            ProductOptionValueId id,
            ProductOptionId optionId,
            String name,
            Integer price,
            boolean usable,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.optionId = optionId;
        this.name = name;
        this.price = price;
        this.usable = usable;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() {
        return id.id();
    }

    public ProductOptionValueId optionValueId() {
        return id;
    }

    public ProductOptionId optionId() {
        return optionId;
    }

    public String name() {
        return name;
    }

    public Integer price() {
        return price;
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
}
