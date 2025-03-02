package com.catsriding.store.domain.shared.spec;

public interface Specification<T> {

    boolean isSatisfiedBy(T t);

    Specification<T> and(Specification<T> specification);

    Specification<T> or(Specification<T> specification);

}
