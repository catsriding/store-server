package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;

public interface ProductOptionJpaRepositoryExtension {

    int countBy(ProductOptionIdentifier identifier);

}
