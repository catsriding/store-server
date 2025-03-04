package com.catsriding.store.infra.database.product.repository;

import static com.catsriding.store.infra.database.product.entity.QProductOptionEntity.productOptionEntity;
import static com.catsriding.store.infra.database.product.entity.QProductOptionValueEntity.productOptionValueEntity;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.ProductOptionWithValue;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductOptionJpaRepositoryImpl implements ProductOptionJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductOptionWithValue> fetchOptionsWithValues(ProductOptionIdentifier identifier) {
        return queryFactory
                .select(Projections.constructor(
                        ProductOptionWithValue.class,
                        productOptionEntity.id,
                        productOptionEntity.productId,
                        productOptionEntity.name,
                        productOptionEntity.optionType,
                        productOptionEntity.usable,
                        productOptionEntity.createdAt,
                        productOptionEntity.updatedAt,
                        productOptionValueEntity.id,
                        productOptionValueEntity.name,
                        productOptionValueEntity.price,
                        productOptionValueEntity.usable,
                        productOptionValueEntity.createdAt,
                        productOptionValueEntity.updatedAt
                ))
                .from(productOptionEntity).leftJoin(productOptionValueEntity)
                .on(productOptionEntity.id.eq(productOptionValueEntity.optionId).and(productOptionValueEntity.isDeleted.isFalse()))
                .where(productOptionEntity.productId.eq(identifier.productId().id()),
                        productOptionEntity.isDeleted.isFalse())
                .orderBy(productOptionEntity.id.asc())
                .fetch();
    }

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
