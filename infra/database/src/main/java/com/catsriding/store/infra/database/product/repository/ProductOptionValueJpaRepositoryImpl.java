package com.catsriding.store.infra.database.product.repository;

import static com.catsriding.store.infra.database.product.entity.QProductOptionValueEntity.productOptionValueEntity;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.infra.database.product.entity.ProductOptionValueEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductOptionValueJpaRepositoryImpl implements ProductOptionValueJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductOptionValueEntity> fetchAllBy(ProductOptionIdentifier identifier) {
        return queryFactory
                .select(productOptionValueEntity)
                .from(productOptionValueEntity)
                .where(
                        productOptionValueEntity.optionId.eq(identifier.optionId().id()),
                        productOptionValueEntity.isDeleted.isFalse()
                )
                .fetch();
    }
}
