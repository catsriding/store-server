package com.catsriding.store.domain.product.spec.option;

import com.catsriding.store.domain.product.ProductOptionType;
import java.util.List;

public interface ProductOptionContext {

    Long optionId();

    Long productId();

    Long sellerId();

    String name();

    ProductOptionType optionType();

    boolean usable();

    List<? extends ProductOptionValueContext> optionValues();
}
