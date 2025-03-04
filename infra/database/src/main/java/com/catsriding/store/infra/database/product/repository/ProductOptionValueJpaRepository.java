package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.infra.database.product.entity.ProductOptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionValueJpaRepository extends JpaRepository<ProductOptionValueEntity, Long> {

}