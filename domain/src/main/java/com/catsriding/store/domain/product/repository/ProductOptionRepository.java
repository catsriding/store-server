package com.catsriding.store.domain.product.repository;

import com.catsriding.store.domain.product.ProductOption;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import java.util.List;

public interface ProductOptionRepository {

    ProductOption save(ProductOption productOption);

    List<ProductOptionWithValue> loadProductOptions(ProductOptionIdentifier identifier);

    int countBy(ProductOptionIdentifier identifier);

}
