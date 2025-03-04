package com.catsriding.store.web.api.product.request;

import com.catsriding.store.application.product.model.ProductOptionDelete;
import com.catsriding.store.web.shared.LoginUser;

public record ProductOptionDeleteRequest(
        Long productId,
        Long optionId,
        Long userId
) {

    public static ProductOptionDeleteRequest from(Long productId, Long optionId, LoginUser user) {
        return new ProductOptionDeleteRequest(productId, optionId, user.id());
    }

    public ProductOptionDelete toCommand() {
        return new ProductOptionDelete(optionId, productId, userId);
    }
}
