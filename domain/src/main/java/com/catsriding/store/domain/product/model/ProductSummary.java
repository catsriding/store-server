package com.catsriding.store.domain.product.model;

public record ProductSummary(
        String productId,
        String sellerId,
        String name,
        int price
) {

    public ProductSummary(Long productId, Long sellerId, String name, int price) {
        this(String.valueOf(productId), String.valueOf(sellerId), name, price);
    }
}
