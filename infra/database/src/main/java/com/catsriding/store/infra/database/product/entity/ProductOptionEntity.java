package com.catsriding.store.infra.database.product.entity;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.ProductOptionId;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.ProductOptionValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_options")
public class ProductOptionEntity {

    @Id
    @Column(name = "id", updatable = false, columnDefinition = "bigint unsigned")
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "option_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductOptionType optionType;

    @Column(name = "usable", nullable = false)
    private boolean usable;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static ProductOptionEntity from(ProductOption domain) {
        ProductOptionEntity entity = new ProductOptionEntity();
        entity.id = domain.id();
        entity.productId = domain.productId().id();
        entity.name = domain.name();
        entity.optionType = domain.optionType();
        entity.usable = domain.usable();
        entity.isDeleted = domain.isDeleted();
        entity.createdAt = domain.createdAt();
        entity.updatedAt = domain.updatedAt();
        return entity;
    }

    public ProductOption toDomain(List<ProductOptionValue> optionValues) {
        return ProductOption.builder()
                .id(ProductOptionId.withId(id))
                .productId(ProductId.withId(productId))
                .name(name)
                .optionType(optionType)
                .usable(usable)
                .isDeleted(isDeleted)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .optionValues(optionValues)
                .build();
    }
}