package com.catsriding.store.infra.database.product.entity;

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

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @Override
    public Long getId() {
        return id;
    }
}