package com.catsriding.store.domain.shared;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseId<U extends BaseId<U, T>, T> {

    private final T value;

    public T id() {
        return value;
    }
}
