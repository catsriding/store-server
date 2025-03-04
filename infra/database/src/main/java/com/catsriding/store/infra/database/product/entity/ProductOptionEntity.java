package com.catsriding.store.infra.database.product.entity;

import com.catsriding.store.domain.product.ProductOptionType;
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

}