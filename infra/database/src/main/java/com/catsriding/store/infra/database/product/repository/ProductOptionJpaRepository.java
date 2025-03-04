package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.infra.database.product.entity.ProductOptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOptionEntity, Long>,
        ProductOptionJpaRepositoryExtension {

    List<ProductOptionEntity> id(Long id);
}