package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.model.ProductOptionIdentifier;
import com.catsriding.store.domain.product.model.UpdateProductOption;
import com.catsriding.store.domain.product.model.UpdateProductOption.UpdateProductOptionValue;
import java.util.List;

public record ProductOptionUpdate(
        Long optionId,
        Long productId,
        Long sellerId,
        String name,
        String optionType,
        boolean usable,
        List<OptionValue> optionValues
) {

    public ProductOptionIdentifier toIdentifier() {
        return new ProductOptionIdentifier(optionId, productId, sellerId);
    }

    public UpdateProductOption toUpdateOption() {
        return new UpdateProductOption(optionId, productId, sellerId, name, optionType, usable, toUpdateOptionValues());
    }

    private List<UpdateProductOptionValue> toUpdateOptionValues() {
        return optionValues.stream()
                .map(value -> new UpdateProductOptionValue(value.name, value.price, value.usable))
                .toList();
    }

    public record OptionValue(
            String name,
            int price,
            boolean usable
    ) {

    }
}
