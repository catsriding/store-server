package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class ProductEntityRepository implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductEntityRepository(
            ProductJpaRepository productJpaRepository
    ) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.from(product);
        entity = productJpaRepository.save(entity);

        log.info("save: Successfully saved product - productId={}, sellerId={}",
                product.productId().id(),
                product.sellerId().id());
        return entity.toDomain();
    }

}
