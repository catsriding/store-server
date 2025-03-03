package com.catsriding.store.infra.database.product.repository;

import static com.catsriding.store.infra.database.product.entity.QProductEntity.productEntity;

import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductJpaRepositoryImpl implements ProductJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProductEntity> fetchBy(ProductIdentifier identifier) {
        return Optional.ofNullable(
                queryFactory
                        .select(productEntity)
                        .from(productEntity)
                        .where(
                                productEntity.id.eq(identifier.productId().id()),
                                productEntity.userId.eq(identifier.sellerId().id()),
                                productEntity.isDeleted.isFalse()
                        )
                        .fetchOne());
    }
}
