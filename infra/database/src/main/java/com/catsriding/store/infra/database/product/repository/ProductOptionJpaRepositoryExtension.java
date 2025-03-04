package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import java.util.List;

public interface ProductOptionJpaRepositoryExtension {

    int countBy(ProductOptionIdentifier identifier);

    List<ProductOptionWithValue> fetchOptionsWithValues(ProductOptionIdentifier identifier);

}
