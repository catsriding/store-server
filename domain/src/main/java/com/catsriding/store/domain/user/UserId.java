package com.catsriding.store.domain.user;

import com.catsriding.store.domain.shared.BaseId;
import com.catsriding.store.domain.shared.sonyflake.core.Sonyflake;

public class UserId extends BaseId<UserId, Long> {

    public UserId(Long value) {
        super(value);
    }

    public static UserId withId(Long id) {
        return new UserId(id);
    }

    public static UserId withoutId() {
        return new UserId(Sonyflake.getInstance().nextId());
    }
}
