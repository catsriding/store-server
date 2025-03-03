package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import java.util.Optional;

public interface ProductJpaRepositoryExtension {

    Optional<ProductEntity> fetchBy(ProductIdentifier identifier);

}
