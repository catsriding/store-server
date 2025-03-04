package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.user.UserId;

public record NewProduct(
        UserId sellerId,
        String name,
        String description,
        int price,
        int deliveryFee,
        ProductStatusType statusType,
        boolean isDeleted
) {

}
