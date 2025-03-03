package com.catsriding.store.domain.product.model;

public record ProductPageCond(
        Long sellerId,
        int pageNumber,
        int pageSize
) {

}
