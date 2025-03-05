package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.infra.database.product.entity.ProductOptionValueEntity;
import java.util.List;

public interface ProductOptionValueJpaRepositoryExtension {

    List<ProductOptionValueEntity> fetchAllBy(ProductOptionIdentifier identifier);
}
