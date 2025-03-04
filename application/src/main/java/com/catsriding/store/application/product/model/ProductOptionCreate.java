package com.catsriding.store.application.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.product.model.NewProductOption;
import com.catsriding.store.domain.product.model.NewProductOptionValue;
import com.catsriding.store.domain.user.UserId;
import java.util.List;

public record ProductOptionCreate(
        Long productId,
        Long sellerId,
        String name,
        String optionType,
        boolean usable,
        List<OptionValue> optionValues
) {

    public NewProductOption toNewOption() {
        return new NewProductOption(
                ProductId.withId(productId),
                UserId.withId(sellerId),
                name,
                ProductOptionType.valueOf(optionType),
                usable,
                toNewOptionValues()
        );
    }

    private List<NewProductOptionValue> toNewOptionValues() {
        return optionValues.stream()
                .map(value -> new NewProductOptionValue(value.name, value.price, value.usable))
                .toList();
    }

    public record OptionValue(
            String name,
            int price,
            boolean usable
    ) {

    }

}
