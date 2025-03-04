package com.catsriding.store.domain.product;

import com.catsriding.store.domain.product.model.NewProductOption;
import com.catsriding.store.domain.product.model.NewProductOptionValue;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.UserId;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class ProductOption {

    private final ProductOptionId id;
    private final ProductId productId;
    private final UserId sellerId;
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
            UserId sellerId,
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
        this.sellerId = sellerId;
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

    public UserId sellerId() {
        return sellerId;
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

    public static ProductOption from(NewProductOption newProductOption, ClockHolder clock) {
        ProductOptionId optionId = ProductOptionId.withoutId();
        List<ProductOptionValue> optionValues = toNewOptionValues(
                newProductOption.optionType(),
                newProductOption.newOptionValues(),
                optionId, clock
        );
        return ProductOption.builder()
                .id(optionId)
                .productId(newProductOption.productId())
                .sellerId(newProductOption.sellerId())
                .name(newProductOption.name())
                .optionType(newProductOption.optionType())
                .usable(newProductOption.usable())
                .isDeleted(false)
                .createdAt(clock.now())
                .updatedAt(clock.now())
                .optionValues(optionValues)
                .build();
    }

    private static List<ProductOptionValue> toNewOptionValues(
            ProductOptionType optionType,
            List<NewProductOptionValue> newOptionValues,
            ProductOptionId optionId,
            ClockHolder clock
    ) {
        return switch (optionType) {
            case INPUT -> List.of();
            case SELECT -> newOptionValues.stream()
                    .map(optionValue -> ProductOptionValue.from(optionValue, optionId, clock))
                    .toList();
        };
    }

    public ProductOptionIdentifier toIdentifier() {
        return new ProductOptionIdentifier(id, productId, sellerId);
    }
}
