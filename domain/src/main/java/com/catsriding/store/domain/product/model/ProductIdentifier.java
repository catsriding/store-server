package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.user.UserId;

public record ProductIdentifier(
        ProductId productId,
        UserId sellerId
) {

    public ProductIdentifier(Long productId, Long userId) {
        this(ProductId.withId(productId), UserId.withId(userId));
    }
}
