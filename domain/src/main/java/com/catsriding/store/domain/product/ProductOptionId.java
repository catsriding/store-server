package com.catsriding.store.domain.product;

import com.catsriding.store.domain.shared.BaseId;
import com.catsriding.store.domain.shared.sonyflake.core.Sonyflake;

public class ProductOptionId extends BaseId<ProductOptionId, Long> {

    public ProductOptionId(Long value) {
        super(value);
    }

    public static ProductOptionId withId(Long id) {
        return new ProductOptionId(id);
    }

    public static ProductOptionId withoutId() {
        return new ProductOptionId(Sonyflake.getInstance().nextId());
    }
}
