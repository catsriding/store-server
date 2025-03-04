package com.catsriding.store.domain.product;

import com.catsriding.store.domain.shared.BaseId;
import com.catsriding.store.domain.shared.sonyflake.core.Sonyflake;

public class ProductOptionValueId extends BaseId<ProductOptionValueId, Long> {

    public ProductOptionValueId(Long value) {
        super(value);
    }

    public static ProductOptionValueId withId(Long id) {
        return new ProductOptionValueId(id);
    }

    public static ProductOptionValueId withoutId() {
        return new ProductOptionValueId(Sonyflake.getInstance().nextId());
    }
}
