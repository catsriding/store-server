package com.catsriding.store.infra.database.product.repository;

import static com.catsriding.store.infra.database.product.entity.QProductOptionEntity.productOptionEntity;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductOptionJpaRepositoryImpl implements ProductOptionJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    @Override
    public int countBy(ProductOptionIdentifier identifier) {
        Long countResult = queryFactory
                .select(productOptionEntity.id.count())
                .from(productOptionEntity)
                .where(
                        productOptionEntity.productId.eq(identifier.productId().id()),
                        productOptionEntity.isDeleted.isFalse()
                )
                .fetchFirst();
        return countResult == null ? 0 : countResult.intValue();
    }
}
