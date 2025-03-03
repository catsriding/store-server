package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductJpaRepositoryExtension {

    Optional<ProductEntity> fetchBy(ProductIdentifier identifier);

    Page<ProductSummary> fetchBy(ProductPageCond cond);

}
