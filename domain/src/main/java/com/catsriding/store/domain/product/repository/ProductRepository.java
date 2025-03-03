package com.catsriding.store.domain.product.repository;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.model.ProductIdentifier;

public interface ProductRepository {

    Product save(Product product);

    Product loadProduct(ProductIdentifier identifier);
}
