package com.catsriding.store.infra.database.product.entity;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.user.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "id", updatable = false, columnDefinition = "bigint unsigned")
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "delivery_fee", nullable = false)
    private Integer deliveryFee;

    @Column(name = "status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatusType statusType;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static ProductEntity from(Product domain) {
        ProductEntity entity = new ProductEntity();
        entity.id = domain.id();
        entity.userId = domain.sellerId().id();
        entity.name = domain.name();
        entity.description = domain.description();
        entity.price = domain.price();
        entity.deliveryFee = domain.deliveryFee();
        entity.statusType = domain.status();
        entity.isDeleted = domain.isDeleted();
        entity.createdAt = domain.createdAt();
        entity.updatedAt = domain.updatedAt();
        return entity;
    }

    public Product toDomain() {
        return Product.builder()
                .id(ProductId.withId(id))
                .sellerId(UserId.withId(userId))
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .statusType(statusType)
                .isDeleted(isDeleted)
                .updatedAt(updatedAt)
                .createdAt(createdAt)
                .build();
    }
}