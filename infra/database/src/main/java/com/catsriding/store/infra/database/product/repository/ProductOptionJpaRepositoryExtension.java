package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.catsriding.store.infra.database.product.entity.ProductOptionEntity;
import java.util.List;
import java.util.Optional;

public interface ProductOptionJpaRepositoryExtension {

    Optional<ProductOptionEntity> fetchBy(ProductOptionIdentifier identifier);

    List<ProductOptionWithValue> fetchOptionsWithValues(ProductOptionIdentifier identifier);

    int countBy(ProductOptionIdentifier identifier);
}
