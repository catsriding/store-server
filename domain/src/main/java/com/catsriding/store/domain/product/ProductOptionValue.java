package com.catsriding.store.domain.product;

import com.catsriding.store.domain.product.model.NewProductOptionValue;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import com.catsriding.store.domain.shared.ClockHolder;
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

    public static ProductOptionValue from(
            NewProductOptionValue optionValue,
            ProductOptionId optionId,
            ClockHolder clock
    ) {
        return ProductOptionValue.builder()
                .id(ProductOptionValueId.withoutId())
                .optionId(optionId)
                .name(optionValue.name())
                .price(optionValue.price())
                .usable(optionValue.usable())
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .build();
    }

    public static ProductOptionValue from(
            UpdateProductOptionValue optionValue,
            ProductOptionId optionId,
            ClockHolder clock
    ) {
        return ProductOptionValue.builder()
                .id(ProductOptionValueId.withoutId())
                .optionId(optionId)
                .name(optionValue.name())
                .price(optionValue.price())
                .usable(optionValue.usable())
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .build();
    }

    public ProductOptionValue update(UpdateProductOptionValue updateOptionValue, ClockHolder clock) {
        return ProductOptionValue.builder()
                .id(id)
                .optionId(optionId)
                .name(updateOptionValue.name())
                .price(updateOptionValue.price())
                .usable(updateOptionValue.usable())
                .isDeleted(false)
                .createdAt(createdAt)
                .updatedAt(clock.now())
                .build();
    }

    public ProductOptionValue delete(ClockHolder clock) {
        return ProductOptionValue.builder()
                .id(id)
                .optionId(optionId)
                .name(name)
                .price(price)
                .usable(usable)
                .isDeleted(true)
                .createdAt(createdAt)
                .updatedAt(clock.now())
                .build();
    }
}
