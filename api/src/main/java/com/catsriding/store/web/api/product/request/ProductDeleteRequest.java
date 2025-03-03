package com.catsriding.store.web.api.product.request;

import com.catsriding.store.application.product.model.ProductDelete;
import com.catsriding.store.web.shared.LoginUser;

public record ProductDeleteRequest(
        Long productId,
        Long userId
) {

    public static ProductDeleteRequest from(Long productId, LoginUser user) {
        return new ProductDeleteRequest(productId, user.id());
    }

    public ProductDelete toCommand() {
        return new ProductDelete(productId, userId);
    }
}
