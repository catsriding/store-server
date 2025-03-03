package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductStatusType;
import com.catsriding.store.domain.product.model.ProductIdentifier;
import com.catsriding.store.domain.product.model.UpdateProduct;
import com.catsriding.store.domain.user.UserId;

public record ProductUpdate(
        Long productId,
        Long sellerId,
        String name,
        String description,
        Integer price,
        Integer deliveryFee,
        String statusType
) {

    public ProductIdentifier toIdentifier() {
        return new ProductIdentifier(ProductId.withId(productId), UserId.withId(sellerId));
    }

    public UpdateProduct toUpdateProduct() {
        return new UpdateProduct(
                ProductId.withId(productId),
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