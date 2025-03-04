package com.catsriding.store.domain.product.repository;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.domain.shared.PagedData;

public interface ProductRepository {

    Product save(Product product);

    Product loadProduct(ProductIdentifier identifier);

    PagedData<ProductSummary> loadPagedProducts(ProductPageCond cond);

    boolean existsBy(ProductOptionIdentifier identifier);

}
