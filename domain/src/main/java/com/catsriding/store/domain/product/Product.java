package com.catsriding.store.domain.product;

import com.catsriding.store.domain.product.model.NewProduct;
import com.catsriding.store.domain.product.model.UpdateProduct;
import com.catsriding.store.domain.product.spec.ProductRegistrationSpec;
import com.catsriding.store.domain.product.spec.ProductUpdateSpec;
import com.catsriding.store.domain.shared.ClockHolder;
import com.catsriding.store.domain.user.UserId;
import java.time.LocalDateTime;
import lombok.Builder;

public class Product {

    private final ProductId id;
    private final UserId sellerId;
    private final String name;
    private final String description;
    private final Integer price;
    private final Integer deliveryFee;
    private final ProductStatusType status;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public Product(
            ProductId id,
            UserId sellerId,
            String name,
            String description,
            Integer price,
            Integer deliveryFee,
            ProductStatusType status,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product from(NewProduct newProduct, ClockHolder clockHolder) {
        new ProductRegistrationSpec().check(newProduct);

        return Product.builder()
                .id(ProductId.withoutId())
                .sellerId(newProduct.sellerId())
                .name(newProduct.name())
                .description(newProduct.description())
                .price(newProduct.price())
                .deliveryFee(newProduct.deliveryFee())
                .status(newProduct.status())
                .isDeleted(newProduct.isDeleted())
                .updatedAt(clockHolder.now())
                .createdAt(clockHolder.now())
                .build();
    }

    public Product update(UpdateProduct updateProduct, ClockHolder clockHolder) {
        new ProductUpdatableSpec().check(this);
        new ProductUpdateSpec().check(updateProduct);

        return Product.builder()
                .id(id)
                .sellerId(sellerId)
                .name(updateProduct.name())
                .description(updateProduct.description())
                .price(updateProduct.price())
                .deliveryFee(updateProduct.deliveryFee())
                .status(updateProduct.status())
                .isDeleted(updateProduct.isDeleted())
                .updatedAt(clockHolder.now())
                .createdAt(createdAt)
                .build();
    }

    public Long id() {
        return id.id();
    }

    public UserId sellerId() {
        return sellerId;
    }

    public ProductId productId() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Integer price() {
        return price;
    }

    public Integer deliveryFee() {
        return deliveryFee;
    }

    public ProductStatusType status() {
        return status;
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
