package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.user.UserId;

public record UpdateProduct(
        ProductId productId,
        UserId userId,
        String name,
        String description,
        Integer price,
        Integer deliveryFee,
        ProductStatusType status,
        boolean isDeleted
) {

}
