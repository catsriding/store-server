package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.model.NewProduct;
import com.catsriding.store.domain.user.UserId;

public record ProductRegistration(
        Long sellerId,
        String name,
        String description,
        int price,
        int deliveryFee,
        String statusType
) {

    public NewProduct toNewProduct() {
        return new NewProduct(
                UserId.withId(sellerId),
                name,
                description,
                price,
                deliveryFee,
                ProductStatusType.valueOf(statusType),
                false
        );
    }
}
