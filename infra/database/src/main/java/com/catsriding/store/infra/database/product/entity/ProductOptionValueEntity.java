package com.catsriding.store.infra.database.product.entity;

import com.catsriding.store.domain.product.ProductOptionId;
import com.catsriding.store.domain.product.ProductOptionValue;
import com.catsriding.store.domain.product.ProductOptionValueId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Persistable;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_option_values")
public class ProductOptionValueEntity implements Persistable<Long> {

    @Id
    @Column(name = "id", updatable = false, columnDefinition = "bigint unsigned")
    private Long id;

    @Column(name = "option_id", nullable = false, updatable = false)
    private Long optionId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "usable", nullable = false)
    private boolean usable;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNewEntity = false;

    public static ProductOptionValueEntity newEntity(ProductOptionValue domain) {
        return from(domain, true);
    }

    public static ProductOptionValueEntity updateEntity(ProductOptionValue domain) {
        return from(domain, false);
    }

    private static ProductOptionValueEntity from(ProductOptionValue domain, boolean isNewEntity) {
        ProductOptionValueEntity entity = new ProductOptionValueEntity();
        entity.id = domain.id();
        entity.optionId = domain.optionId().id();
        entity.name = domain.name();
        entity.price = domain.price();
        entity.usable = domain.usable();
        entity.isDeleted = domain.isDeleted();
        entity.createdAt = domain.createdAt();
        entity.updatedAt = domain.updatedAt();
        entity.isNewEntity = isNewEntity;
        return entity;
    }

    public ProductOptionValue toDomain() {
        return ProductOptionValue.builder()
                .id(ProductOptionValueId.withId(id))
                .optionId(ProductOptionId.withId(optionId))
                .name(name)
                .price(price)
                .usable(usable)
                .isDeleted(isDeleted)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @Override
    public Long getId() {
        return id;
    }
}