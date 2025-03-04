package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;

public record ProductOptionDelete(
        Long optionId,
        Long productId,
        Long userId
) {

    public ProductOptionIdentifier toIdentifier() {
        return new ProductOptionIdentifier(optionId, productId, userId);
    }
}
