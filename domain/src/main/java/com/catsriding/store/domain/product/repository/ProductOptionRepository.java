package com.catsriding.store.domain.product.repository;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;

public interface ProductOptionRepository {

    ProductOption save(ProductOption productOption);

    int countBy(ProductOptionIdentifier identifier);
}
