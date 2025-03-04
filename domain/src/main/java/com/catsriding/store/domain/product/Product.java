package com.catsriding.store.domain.product;

import static com.catsriding.store.domain.product.ProductStatusType.DELETED;

import com.catsriding.store.domain.product.model.NewProduct;
import com.catsriding.store.domain.product.model.UpdateProduct;
import com.catsriding.store.domain.product.spec.ProductUpdatableSpec;
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
    private final ProductStatusType statusType;
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
            ProductStatusType statusType,
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
        this.statusType = statusType;
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
                .statusType(newProduct.statusType())
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
                .statusType(updateProduct.statusType())
                .isDeleted(updateProduct.isDeleted())
                .updatedAt(clockHolder.now())
                .createdAt(createdAt)
                .build();
    }

    public Product delete(ClockHolder clockHolder) {
        new ProductUpdatableSpec().check(this);

        return Product.builder()
                .id(id)
                .sellerId(sellerId)
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .statusType(DELETED)
                .isDeleted(true)
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
        return statusType;
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
