package com.catsriding.store.infra.database.product.repository;

import static com.catsriding.store.infra.database.product.entity.QProductEntity.productEntity;

import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.product.model.ProductPageCond;
import com.catsriding.store.domain.product.model.ProductSummary;
import com.catsriding.store.infra.database.product.entity.ProductEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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

    @Override
    public Page<ProductSummary> fetchBy(ProductPageCond cond) {
        Pageable pageable = PageRequest.of(cond.pageNumber(), cond.pageSize());
        List<ProductSummary> content = queryFactory
                .select(Projections.constructor(
                        ProductSummary.class,
                        productEntity.id,
                        productEntity.userId,
                        productEntity.name,
                        productEntity.price
                ))
                .from(productEntity)
                .where(
                        productEntity.userId.eq(cond.sellerId()),
                        productEntity.isDeleted.isFalse()
                )
                .orderBy(productEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(productEntity.id.count())
                .from(productEntity)
                .where(
                        productEntity.userId.eq(cond.sellerId()),
                        productEntity.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
    }
}
