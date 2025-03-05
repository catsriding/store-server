package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.spec.option.ProductOptionContext;
import com.catsriding.store.domain.product.spec.option.ProductOptionValueContext;
import java.util.List;

public record NewProductOption(
        Long productId,
        Long sellerId,
        String name,
        ProductOptionType optionType,
        boolean usable,
        List<NewProductOptionValue> newOptionValues
) implements ProductOptionContext {

    @Override
    public Long optionId() {
        return 0L;
    }

    @Override
    public List<NewProductOptionValue> optionValues() {
        return newOptionValues;
    }

    public record NewProductOptionValue(
            String name,
            int price,
            boolean usable
    ) implements ProductOptionValueContext {

    }

}
