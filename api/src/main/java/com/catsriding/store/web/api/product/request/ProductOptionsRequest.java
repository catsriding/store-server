package com.catsriding.store.web.api.product.request;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.web.shared.LoginUser;

public record ProductOptionsRequest(
        Long productId,
        Long userId
) {

    public static ProductOptionsRequest from(Long productId, LoginUser user) {
        return new ProductOptionsRequest(productId, user.id());
    }

    public ProductOptionIdentifier toCond() {
        return new ProductOptionIdentifier(null, productId, userId);
    }
}
