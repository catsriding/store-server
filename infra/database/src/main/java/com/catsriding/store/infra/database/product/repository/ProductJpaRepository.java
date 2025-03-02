package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.infra.database.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>, ProductJpaRepositoryExtension {

}