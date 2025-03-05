package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.spec.option.ProductOptionContext;
import com.catsriding.store.domain.product.spec.option.ProductOptionValueContext;
import java.util.List;

public record UpdateProductOption(
        Long optionId,
        Long productId,
        Long sellerId,
        String name,
        ProductOptionType optionType,
        boolean usable,
        List<UpdateProductOptionValue> updateOptionValues
) implements ProductOptionContext {

    public UpdateProductOption(
            Long optionId,
            Long productId,
            Long sellerId,
            String name,
            String optionType,
            boolean usable,
            List<UpdateProductOptionValue> updateOptionValues
    ) {
        this(optionId, productId, sellerId, name, ProductOptionType.valueOf(optionType), usable, updateOptionValues);
    }

    @Override
    public List<UpdateProductOptionValue> optionValues() {
        return updateOptionValues;
    }

    public record UpdateProductOptionValue(
            String name,
            int price,
            boolean usable
    ) implements ProductOptionValueContext {

    }
}
