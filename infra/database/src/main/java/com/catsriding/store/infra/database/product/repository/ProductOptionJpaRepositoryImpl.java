package com.catsriding.store.infra.database.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductOptionJpaRepositoryImpl implements ProductOptionJpaRepositoryExtension {

    private final JPAQueryFactory queryFactory;

}
