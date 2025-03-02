package com.catsriding.store.domain.product;

import com.catsriding.store.domain.shared.BaseId;
import com.catsriding.store.domain.shared.sonyflake.core.Sonyflake;

public class ProductId extends BaseId<ProductId, Long> {

    public ProductId(Long value) {
        super(value);
    }

    public static ProductId withId(Long id) {
        return new ProductId(id);
    }

    public static ProductId withoutId() {
        return new ProductId(Sonyflake.getInstance().nextId());
    }
}
