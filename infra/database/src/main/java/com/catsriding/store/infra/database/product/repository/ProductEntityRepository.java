package com.catsriding.store.infra.database.product.repository;

import com.catsriding.store.domain.product.Product;
import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.domain.product.repository.ProductRepository;
import com.catsriding.store.domain.shared.PagedData;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import com.catsriding.store.infra.database.shared.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @Override
    @Transactional(readOnly = true)
    public Product loadProduct(ProductIdentifier identifier) {
        return productJpaRepository.fetchBy(identifier)
                .map(ProductEntity::toDomain)
                .orElseThrow(() -> {
                    log.warn("retrieveBy: Does not found product - productId={}, sellerId={}",
                            identifier.productId().id(),
                            identifier.sellerId().id());
                    return new DataNotFoundException("요청한 상품을 찾을 수 없습니다. 확인 후 다시 확인해주세요.");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ProductSummary> loadPagedProducts(ProductPageCond cond) {
        Page<ProductSummary> page = productJpaRepository.fetchBy(cond);
        return new PagedData<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBy(ProductOptionIdentifier identifier) {
        return productJpaRepository.existsBy(identifier);
    }

}
