package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOptionId;
import com.catsriding.store.domain.user.UserId;

public record ProductOptionIdentifier(
        ProductOptionId optionId,
        ProductId productId,
        UserId sellerId
) {

    public ProductOptionIdentifier(Long optionId, Long productId, Long userId) {
        this(ProductOptionId.withId(optionId), ProductId.withId(productId), UserId.withId(userId));
    }
}
