package com.catsriding.store.web.api.product.request;

import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.web.shared.LoginUser;

public record ProductDetailsRequest(
        Long productId,
        Long userId
) {

    public static ProductDetailsRequest from(Long productId, LoginUser user) {
        return new ProductDetailsRequest(productId, user.id());
    }

    public ProductIdentifier toCond() {
        return new ProductIdentifier(productId, userId);
    }
}
