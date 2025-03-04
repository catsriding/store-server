package com.catsriding.store.domain.product.model;

import com.catsriding.store.domain.product.ProductId;
import com.catsriding.store.domain.product.ProductOptionType;
import com.catsriding.store.domain.user.UserId;
import java.util.List;

public record NewProductOption(
        ProductId productId,
        UserId sellerId,
        String name,
        ProductOptionType optionType,
        boolean usable,
        List<NewProductOptionValue> newOptionValues
) {

}
