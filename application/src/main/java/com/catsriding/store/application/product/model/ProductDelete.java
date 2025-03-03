package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.user.UserId;

public record ProductDelete(
        Long productId,
        Long sellerId
) {

    public ProductIdentifier toIdentifier() {
        return new ProductIdentifier(ProductId.withId(productId), UserId.withId(sellerId));
    }

}
