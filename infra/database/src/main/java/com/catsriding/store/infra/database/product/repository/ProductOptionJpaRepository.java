package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.infra.database.product.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOptionEntity, Long>,
        ProductOptionJpaRepositoryExtension {

}